package dev.utils

import com.typesafe.config.ConfigFactory
import dev.models.Submodule

object AppConfig {
  private lazy val appConfig        = ConfigFactory.load()
  private lazy val scriptsBuildProp = parse(Submodule.SequencerScripts)
  private lazy val eswBuildProp     = parse(Submodule.ESW)

  lazy val ScriptsVersion: String = readVersion(appConfig.getString("scripts.version"))
  lazy val EswVersion: String     = readVersion(scriptsBuildProp.getString("esw.version"))
  lazy val CswVersion: String     = readVersion(eswBuildProp.getString("csw.version"))

  private def parse(submodule: Submodule) = ConfigFactory.parseFile(submodule.buildProperties.toIO)

  // Assumptions:
  // 1. Version Scheme: x.y.z
  // 2. sha does not contains '.'
  private def isTagged(sha: String) = sha.split('.').length >= 2

  private def readVersion(sha: String) =
    if (isTagged(sha)) "v" + sha
    else sha
}
