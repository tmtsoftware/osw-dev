package dev.apps

import caseapp.{AppName, CommandApp, HelpMessage, ProgName, RemainingArgs}
import dev.utils.Git

sealed trait Command

@HelpMessage("Initializes repo for the first time")
case object Init extends Command

@HelpMessage("Starts csw-services and esw eng-ui-services")
case class Start(live: Boolean = false) extends Command

@HelpMessage("Updates all submodules to their correct versions")
case object UpdateSubmodules extends Command

@HelpMessage("Prints versions compatibility table")
case object PrintVersions extends Command

object TMTRunner extends CommandApp[Command] {
  override def appName: String    = getClass.getSimpleName.dropRight(1)
  override def progName: String   = "tmt-runner"
  override def appVersion: String = "0.1.0-SNAPSHOT"

  def run(command: Command, args: RemainingArgs): Unit = command match {
    case Init             => Git.initSubmodules()
    case UpdateSubmodules => GitUpdateSubmodules.update()
    case PrintVersions    => Versions.prettyPrint()
    case Start(live)      => ServicesLauncher.launch(live)
  }
}
