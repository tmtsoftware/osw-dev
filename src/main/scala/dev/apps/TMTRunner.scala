package dev.apps

import caseapp.{AppName, CommandApp, HelpMessage, ProgName, RemainingArgs}

@AppName("TMTRunner")
@ProgName("TMTRunner")
sealed trait Command

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
    case Start(live)      => ServicesLauncher.launch(live)
    case UpdateSubmodules => GitUpdateSubmodules.update()
    case PrintVersions    => Versions.prettyPrint()
  }
}
