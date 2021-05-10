package dev.utils

import dev.models.Submodule
import os.CommandResult

object Git {
  def pull(submodule: Submodule): CommandResult = {
    Logger.log(Console.YELLOW, submodule.color, "Pulling latest changes ...")
    os.proc("git", "-C", submodule.name, "pull", "--rebase")
      .call(stdout = os.ProcessOutput.Readlines(Logger.log(submodule.color, submodule.name, _)))
  }

  def checkout(sha: String, submodule: Submodule): CommandResult = {
    val command = Seq("git", "-C", submodule.name, "checkout", sha)
    println(s"[$submodule] " + command.mkString(" "))
    os.proc(command).call(stdout = os.ProcessOutput.Readlines(Logger.log(submodule.color, submodule.name, _)))
  }

  def head(submodule: Submodule): String =
    os.proc("git", "-C", submodule.name, "rev-parse", "--short", "HEAD").call().out.text()
}
