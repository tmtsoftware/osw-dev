package dev.apps

import dev.models.Submodule.{CSW, ESW, SequencerScripts}
import dev.utils.{AppConfig, Git}

object GitUpdateSubmodules {
  private def checkoutSequencerScripts() =
    if (AppConfig.ScriptsVersion == "master" | AppConfig.ScriptsVersion == "main")
      Git.pull(SequencerScripts)
    else Git.checkout(SequencerScripts, AppConfig.ScriptsVersion)

  def update(): Unit = {
    checkoutSequencerScripts()
    Git.checkout(ESW, AppConfig.EswVersion)
    Git.checkout(CSW, AppConfig.CswVersion)
  }
}
