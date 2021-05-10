package dev.utils

import java.io.{File, FileReader}
import java.util.Properties
import scala.util.Using

object PropertiesReader {
  def read(key: String, file: File): String =
    Using.resource(new FileReader(file)) { reader =>
      val properties = new Properties()
      properties.load(reader)
      properties.getProperty(key)
    }
}
