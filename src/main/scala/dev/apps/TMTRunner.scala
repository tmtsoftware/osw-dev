package dev.apps

import dev.models.Submodule.{CSW, ESW}
import dev.utils.{Logger, Sbt}
import os.SubProcess

object TMTRunner {
  def main(args: Array[String]): Unit = {
    lazy val cswServicesOpt = Sbt.run(
      CSW,
      "Successfully started Config Service",
      "csw-services/run start -c"
    )

    lazy val eswServicesOpt = Sbt.run(
      ESW,
      "Successfully started sequence-manager",
      "esw-services/run start-eng-ui-services"
    )

    cswServicesOpt match {
      case Some(_) =>
        eswServicesOpt match {
          case Some(eswServices) =>
            eswServices.join()
            exit("Stopped csw-services, Reason: esw-services terminated.", cswServicesOpt)
          case None =>
            exit("Stopped csw-services, Reason: esw-services terminated.", cswServicesOpt)
        }
      case None => exit("Failed to start csw-services.")
    }
  }

  private def exit(reason: String, process: Option[SubProcess] = None): Unit = {
    process.foreach(_.destroyForcibly())
    Logger.logRed(s"[error] $reason")
    System.exit(1)
  }
}
