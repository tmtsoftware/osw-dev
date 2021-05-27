package dev.models

import os.Path

sealed abstract class Submodule(val name: String, val branch: String, val color: String) {
  private val wd: Path = os.pwd

  def dir: Path             = wd / name
  def buildProperties: Path = dir / "project" / "build.properties"
}

object Submodule {
  object CSW              extends Submodule("csw", "master", Console.YELLOW)
  object ESW              extends Submodule("esw", "master", Console.CYAN)
  object SequencerScripts extends Submodule("sequencer-scripts", "ui-setup", Console.BLUE)
  object OcsEngUi         extends Submodule("esw-ocs-eng-ui", "main", Console.MAGENTA)
}
