package dev.apps

import dev.models.Submodule.{CSW, ESW, SequencerScripts}
import dev.utils.{Git, PropertiesReader, Tabulator}

object Versions {
  def eswVersion: String =
    PropertiesReader.read("esw.version", SequencerScripts.buildProperties.toIO).trim
  def cswVersion: String              = PropertiesReader.read("csw.version", ESW.buildProperties.toIO).trim
  def sequencerScriptsVersion: String = Git.head(SequencerScripts).trim

  def printTable: String =
    Tabulator.formatTable(
      List(
        List("Module", "Version"),
        List(SequencerScripts.name, sequencerScriptsVersion),
        List(ESW.name, eswVersion),
        List(CSW.name, cswVersion)
      )
    )
}

object VersionGenerator {
  def main(args: Array[String]): Unit = {
    println(Versions.printTable)
  }
}
