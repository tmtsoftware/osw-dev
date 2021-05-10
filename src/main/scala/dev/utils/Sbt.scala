package dev.utils

import dev.models.Submodule
import os.SubProcess

object Sbt {
  private val sbt = "sbt"
  // FIXME make it os specific
  private val env = Map("INTERFACE_NAME" -> "en0", "AAS_INTERFACE_NAME" -> "en0")

  def run(
      submodule: Submodule,
      waitForOutput: String,
      cmd: String*
  ): Option[SubProcess] = {
    Logger.logGreen(s"========= Starting ${submodule.name}-services =========")
    @volatile var finished = false

    val process =
      os
        .proc(sbt, cmd)
        .spawn(
          env = env,
          cwd = submodule.dir,
          stdout = os.ProcessOutput.Readlines { line =>
            Logger.log(submodule, line)
            if (line.contains(waitForOutput)) finished = true
          }
        )

    sys.addShutdownHook(process.destroyForcibly())
    while (!finished && process.isAlive()) Thread.sleep(100)
    if(process.isAlive()) Some(process) else None
  }
}
