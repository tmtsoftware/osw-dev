package dev.utils

import scala.jdk.OptionConverters.RichOptional
import scala.util.control.NonFatal

object ProcessUtils {

  private lazy val jps            = os.proc("jps", "-lm").call().out.lines()
  private lazy val keycloakPid    = processHandleFor("embedded-keycloak")
  private lazy val cswServicesPid = processHandleFor("Main start -c")
  private lazy val eswServicesPid = processHandleFor("EswServicesApp")
  private lazy val smPid          = processHandleFor("SequenceManagerApp")

  private lazy val runningProcesses: Map[String, ProcessHandle] = Map(
    "Sequence Manager" -> smPid,
    "ESW Services"     -> eswServicesPid,
    "CSW Services"     -> cswServicesPid,
    "Keycloak"         -> keycloakPid
  ).collect { case (entry, Some(pid)) => entry -> pid }

  def cleanupOldProcesses(): Unit =
    try {
      if (runningProcesses.nonEmpty) {
        Logger.logRed("Running instances for the following applications detected!")
        println(tabulatedView())

        Logger.logYellow("Do you want to kill these PID's and continue? (y/n)")
        val userInput: String = scala.io.StdIn.readLine()
        if (userInput == "y") killRunningProcesses()
        else if (userInput == "n")
          Logger.logRed("Continuing without killing existing PID's, this may fail!")
        else {
          Logger.logRed(s"Invalid input '$userInput' provided, exiting ...")
          System.exit(1)
        }
      }
    } catch {
      case NonFatal(e) =>
        e.printStackTrace()
        Logger.logRed(
          "Something went wrong with cleaning up old processes task, skipping it!"
        )
    }

  private def killRunningProcesses(): Unit = runningProcesses.foreach { case (name, handle) =>
    handle.destroyForcibly()
    Logger.logYellow(s"Killed PID=${handle.pid()} of application $name")
  }

  private def tabulatedView(): String = {
    val appToPid =
      runningProcesses.view.mapValues(_.pid().toString).toSeq.map(kv => Seq(kv._1, kv._2))
    Tabulator.formatTable(Seq("App Name", "PID") +: appToPid)
  }

  private def processHandleFor(name: String) = {
    val pid = jps.find(_.contains(name)).flatMap(_.split(" ").headOption)
    pid.map { id =>
      val validPid =
        id.toIntOption.getOrElse(throw new RuntimeException(s"Parsing PID=$pid to Int failed"))
      val ph = ProcessHandle.of(validPid).toScala
      ph.getOrElse(throw new RuntimeException(s"Failed to create ProcessHandle from PID=$validPid"))
    }
  }

}
