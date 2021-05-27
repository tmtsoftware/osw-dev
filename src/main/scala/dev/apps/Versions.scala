package dev.apps

import dev.models.Submodule.{CSW, ESW, SequencerScripts}
import dev.utils.{AppConfig, Git, Tabulator}

object Versions {
  def scriptsSha: String = Git.head(SequencerScripts).trim

  private def tabulatedVersions: String =
    Tabulator.formatTable(
      List(
        List("Module", "Branch", "Version"),
        List(SequencerScripts.name, SequencerScripts.branch, scriptsSha),
        List(ESW.name, ESW.branch, AppConfig.EswVersion),
        List(CSW.name, CSW.branch, AppConfig.CswVersion)
      )
    )

  def printTabulated(): Unit = {
    println(Console.YELLOW)
    println(tabulatedVersions)
    println(Console.RESET)
  }
}
