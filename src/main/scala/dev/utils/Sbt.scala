package dev.utils

import dev.models.Submodule
import os.SubProcess

object Sbt {
  private val sbt = "sbt"

  def run(
      submodule: Submodule,
      waitForOutput: String,
      cmd: String*
  ): SubProcess = {
    Logger.logGreen(s"Starting ${submodule.name}-services")
    @volatile var finished = false
    val process =
      os
        .proc(sbt, cmd)
        .spawn(
          cwd = submodule.dir,
          mergeErrIntoOut = true,
          stdout = os.ProcessOutput.Readlines { line =>
            Logger.log(submodule, line)
            if (line.contains(waitForOutput)) finished = true
          }
        )
    sys.addShutdownHook(process.destroyForcibly())

    while (!finished) {
      Thread.sleep(100)
    }

    Logger.logGreen(s"Started ${submodule.name}-services")
    process
  }
}
