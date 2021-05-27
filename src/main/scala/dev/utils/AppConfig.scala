package dev.utils

import com.typesafe.config.ConfigFactory

object AppConfig {
  private lazy val config = ConfigFactory.load()

  lazy val ScriptsVersion: String = config.getString("scripts.version")
}
