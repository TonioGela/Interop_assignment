package assignment

import scala.collection.mutable
import scala.io.BufferedSource

case class CsvRow(
  id: Int,
  name: Option[String],
  surname: Option[String],
  birthyear: Option[Int],
  country: Option[String],
  money: Option[(Int,String)]
)

case class CSVPreviewer(csv: mutable.ArrayBuffer[CsvRow]) {
  def showCSV(): Unit = csv.foreach(println(_))

  def richestAndPoorest(): Unit = {
    var poorest: (String, Double) = ("", Double.MaxValue)
    var richest: (String, Double) = ("", Double.MinValue)
    csv.foreach(line => {
      val amount = line.currency.getOrElse("") match {
        case "GBP" => line.amount.getOrElse(0) / EUconversions.GBP
        case "USD" | "EUR" => line.amount.getOrElse(0)
        case "ITL" => line.amount.getOrElse(0) / EUconversions.ITL
      }
      if (amount > richest._2) richest = (line.name.getOrElse("") + " " + line.surname.getOrElse(""), amount)
      else if (amount < poorest._2) poorest = (line.name.getOrElse("") + " " + line.surname.getOrElse(""), amount)
    })
    println(s"Richest: $richest, Poorest: $poorest")
  }

  def richerOrPoorer(csv2: CSVPreviewer): Unit = {
//    val prova = csv.filter(line1 => csv2.csv.map(line2 => line1.name.getOrElse("") == line2.name.getOrElse("") ||
//            line1.surname.getOrElse("") == line2.surname.getOrElse("")))))
//      csv.filter(line1 => csv2.csv.map(line2 => line1.name.getOrElse("") == line2.name.getOrElse("") ||
//      line1.surname.getOrElse("") == line2.surname.getOrElse("")))
//    prova.foreach(println(_))
  }

  def countryUsers(country:String): Unit = {
    println(s"$country users: " + 
      csv.filter(_.country.getOrElse("") == country).map(line => line.surname.getOrElse(line.name.getOrElse("")))
    )
  }

  def countryCurrency(currencyCheck: String): Unit = {
    println(s"$currencyCheck using countries: " + csv.filter(_.currency.getOrElse("") == currencyCheck).map(_.country.getOrElse("")))
  }

  def topFiveCountries(): Unit ={
    // val countries: mutable.ArrayBuffer[(String, Int)] = 
    //   csv.map { line => 
    //     val country = line.country.getOrElse("")
    //     (country, csv.count(_.country.getOrElse("") == country))
    //   }.distinct.sortWith(_._2 > _._2)
    println("Top five countries: ")
    
    csv.foldLeft(Map.empty[String, Int]){ case (map, line) =>
      map.updatedWith(line.country.getOrElse("")){
        case Some(x) => Some(x + 1)
        case None => Some(1)
      }
    }.toList.take(5).foreach { case (country, count) => println(s"$country -> $count")}
    
    // if (countries.size < 5) println(countries)
    // else for(i <- 0 until 5) println(countries(i))
  }
}

object EUconversions{
  val GBP = 0.8476
  val USD, EUR = 1
  val ITL = 1936.27
}

object CSVChecker {

  def parseCSV(csvSource: List[String]): List[CsvRow] = {
    val lines: List[String] = csvSource.drop(1)  // Skip the headers

    val maybeRows: List[Either[String, CsvRow]] = lines.zipWithIndex.map { case (line, index) => 
      val rawTokens: List[String] = line.split(",", -1).toList

      def getIfNonEmpty(n: Int): Option[String] = {
        if(rawTokens.isDefinedAt(n)) Some(rawTokens(n)).filterNot(_.isBlank).map(_.trim)
        else None
      }

      def getOrElseInt(n: Int): Option[Int] = for {
        s <- getIfNonEmpty(n)
        x <- s.toIntOption
      } yield x

      if (rawTokens.length != 7) Left(s"The line at index $index was shorter than needed")
      else {
        val maybeID: Either[String, Int] =
          getOrElseInt(0).filter(x => x > 0).toRight(s"The id at index $index was not a positive integer")
        
        val maybeHeadOfTheRow: Either[String,(Int, Option[String], Option[String])] = maybeID.flatMap { id =>
          (getIfNonEmpty(1), getIfNonEmpty(2)) match {
            case (None, None) => Left(s"The line at index $index doesn't contain neither a name or a surname")
            case (name, surname) => Right((id, name, surname))
          }
        }

        maybeHeadOfTheRow.map { case (id, name, surname) =>
          val maybeBirthYear: Option[Int] = getOrElseInt(3)
          val maybeCountry: Option[String] = getIfNonEmpty(4)

          val maybeMoney: Option[(Int, String)] = getOrElseInt(5).map { amount =>
            val maybeCurrency: Option[String] = getIfNonEmpty(6)
            (amount, maybeCurrency.getOrElse("EUR"))
          }
        
          CsvRow(id, name, surname, maybeBirthYear, maybeCountry, maybeMoney)
        }
      }
    }

    // I left li vogliamo stampare,
    // I right li vogliamo accumulare

    // Usiamo fold accumulando una lista di CsvRow, i left li stampiamo come side effect
    val rows: List[CsvRow] = maybeRows.foldLeft(List.empty[CsvRow]){ case (rows, elem) => 
      elem match {
        case Left(err) =>
          System.err.println(err)
          rows
        case Right(row) => row :: rows
      }
    }

    rows.distinctBy(_.id)
  }
}
