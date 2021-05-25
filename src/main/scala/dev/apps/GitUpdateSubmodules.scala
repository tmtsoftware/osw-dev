package dev.apps

import dev.models.Submodule.{CSW, ESW, SequencerScripts}
import dev.utils.Git

object GitUpdateSubmodules {
  def update(): Unit = {
    Git.pull(SequencerScripts)
    Git.checkout(ESW, Versions.eswVersion)
    Git.checkout(CSW, Versions.cswVersion)
  }
}
