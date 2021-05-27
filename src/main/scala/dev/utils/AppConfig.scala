package dev.utils

import com.typesafe.config.ConfigFactory
import dev.models.Submodule

object AppConfig {
  private lazy val appConfig        = ConfigFactory.load()
  private lazy val scriptsBuildProp = parse(Submodule.SequencerScripts)
  private lazy val eswBuildProp     = parse(Submodule.ESW)

  lazy val ScriptsVersion: String = appConfig.getString("scripts.version")
  lazy val EswVersion: String     = scriptsBuildProp.getString("esw.version")
  lazy val CswVersion: String     = eswBuildProp.getString("csw.version")

  private def parse(submodule: Submodule) = ConfigFactory.parseFile(submodule.buildProperties.toIO)
}
