package assignment


import scala.collection.mutable
import scala.io.BufferedSource

case class csvLine(id: Int, name:Option[String], surname: Option[String], birthyear: Option[Int],
                   country: Option[String], amount: Option[Int], currency: Option[String])

case class CSVPreviewer(csv: mutable.ArrayBuffer[csvLine]) {
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
    println(s"$country users: " + csv.filter(_.country.getOrElse("") == country).map(line => line.surname.getOrElse(line.name.getOrElse(""))))
  }

  def countryCurrency(currencyCheck: String): Unit = {
    println(s"$currencyCheck using countries: " + csv.filter(_.currency.getOrElse("") == currencyCheck).map(_.country.getOrElse("")))
  }

  def topFiveCountries(): Unit ={
    val countries: mutable.ArrayBuffer[(String, Int)] = csv.map(line => (line.country.getOrElse(""), csv.count(_.country.getOrElse("") ==
      line.country.getOrElse("")))).distinct.sortWith(_._2 > _._2)
    println("Top five countries: ")
    if (countries.size < 5) println(countries)
    else for(i <- 0 until 5) println(countries(i))
  }
}

object EUconversions{
  val GBP = 0.8476
  val USD, EUR = 1
  val ITL = 1936.27
}


object CSVChecker{
  def parseCSV(csvSource: BufferedSource): CSVPreviewer = {
    val lines: Iterator[String] = csvSource.getLines().drop(1)  // Skip the headers
    val csvPreviewer:CSVPreviewer = CSVPreviewer(mutable.ArrayBuffer[csvLine]())  // Initialize the structure that will contain the CSV
    val uniqueIds: mutable.Set[Int] = mutable.Set[Int]()  // Set of IDs met while parsing
    for ((line, index) <- lines.zipWithIndex) {
      val lineArray: Array[String] = line.split(",").map(_.trim)

      def getOrElse(n: Int): Option[String] = {
        if(lineArray.isDefinedAt(n)) Some(lineArray(n)).filterNot(_.isEmpty)
        else None
      }

      def getOrElseInt(n: Int): Option[Int] = {
        if (lineArray.isDefinedAt(n)) {
          try {
            Some(lineArray(n).toInt)
          } catch {
            case _: Exception => None
          }
        }
        else None
      }

      // mapping of the array on the csvLine class
      val lineCSV: csvLine = csvLine(getOrElseInt(0).getOrElse(-1), getOrElse(1), getOrElse(2), getOrElseInt(3),
        getOrElse(4), getOrElseInt(5), getOrElse(6))
      lineCSV match {
        case line if line.id < 0 || uniqueIds.contains(line.id) => println(s"The row number $index does not have a valid ID")
        case line if line.name.isEmpty && line.surname.isEmpty => println(s"The row number $index with ID ${line.id} does not have at least one member between name and surname")
        case _ =>
          // Adding the ID to the set of unique IDs
          uniqueIds += lineCSV.id
          // Implementing the last check if the amount is present without the currency
          if (lineCSV.amount.isDefined && lineCSV.currency.isEmpty) {
            println(s"The row number $index does not have a valid currency, EUR is set by default")
            // save the line forcing currency to EUR
            csvPreviewer.csv += csvLine(lineCSV.id, lineCSV.name, lineCSV.surname, lineCSV.birthyear,
              lineCSV.country, lineCSV.amount, Option("EUR"))
          } // All checks passed, save the line
          else csvPreviewer.csv += lineCSV
      }
    }
    csvSource.close
    csvPreviewer
  }
}
