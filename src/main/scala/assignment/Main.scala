package assignment

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    // val csvPreviewer: CSVPreviewer = 
      println(CSVChecker.parseCSV(Source.fromResource("data.csv").getLines().toList).mkString("\n"))
    // println("----------------------")
    // csvPreviewer.showCSV()
    // println("----------------------")
    // csvPreviewer.richestAndPoorest()
    // println("----------------------")
    // csvPreviewer.countryUsers("Greece")
    // println("----------------------")
    // csvPreviewer.countryCurrency("ITL")
    // println("----------------------")
    // csvPreviewer.topFiveCountries()
//    csvPreviewer.richerOrPoorer(CSVChecker.parseCSV(scala.io.Source.fromFile("data2.csv")))
  }
}