package dev.apps

import dev.models.Submodule.{CSW, ESW, SequencerScripts}
import dev.utils.{Git, PropertiesReader, Tabulator}

object Versions {
  val EswVersion: String =
    PropertiesReader.read("esw.version", SequencerScripts.buildProperties.toIO).trim
  val CswVersion: String              = PropertiesReader.read("csw.version", ESW.buildProperties.toIO).trim
  val SequencerScriptsVersion: String = Git.head(SequencerScripts).trim

  def printTable: String =
    Tabulator.formatTable(
      List(
        List("Module", "Version"),
        List(SequencerScripts.name, SequencerScriptsVersion),
        List(ESW.name, EswVersion),
        List(CSW.name, CswVersion)
      )
    )
}

object VersionGenerator {
  def main(args: Array[String]): Unit = {
    println(Versions.printTable)
  }
}
