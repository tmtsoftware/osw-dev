import os.SubProcess

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Logger {
  def log(prefixColor: String, prefix: String, msg: String): Unit =
    println(s"$prefixColor$prefix | ${Console.RESET}$msg")

  def logGreen(msg: String): Unit = println(s"${Console.GREEN}$msg${Console.RESET}")
}

object Sbt {
  private val sbt = "sbt"

  def run(cwd: os.Path, waitForOutput: String, prefixColor: String, prefix: String, cmd: String*): SubProcess = {
    Logger.logGreen(s"Starting $prefix")
    @volatile var finished = false
    val process =
      os
        .proc(sbt, cmd)
        .spawn(
          cwd = cwd, stdin = os.Inherit, stderr = os.Inherit,
          stdout = os.ProcessOutput.Readlines { line =>
            Logger.log(prefixColor, prefix, line)
            if (line.contains(waitForOutput)) finished = true
          }
        )
    sys.addShutdownHook(process.destroyForcibly())

    while (!finished) {
      Thread.sleep(100)
    }

    Logger.logGreen(s"Started $prefix")
    process
  }
}

object Main extends App {
  val wd = os.pwd
  val csw = wd / "csw"
  val esw = wd / "esw"

  val cswServices =
    Sbt.run(
      cwd = csw,
      waitForOutput = "Server online at",
      prefixColor = Console.YELLOW,
      prefix = "CSW-SERVICES",
      cmd = "csw-services/run start -c"
    )

  val eswServices =
    Sbt.run(
      cwd = esw,
      waitForOutput = "Server online at",
      prefixColor = Console.MAGENTA,
      prefix = "ESW-SERVICES",
      cmd = "esw-services/run start-eng-ui-services"
    )

  // if any of the process exits, then kill other
  val f1 = Future {
    cswServices.join()
    eswServices.destroyForcibly()
  }

  val f2 = Future {
    eswServices.join()
    cswServices.destroyForcibly()
  }

  Await.result(f2, Duration.Inf)
  Await.result(f1, Duration.Inf)
}
