package dev.utils

import dev.models.Submodule
import ColoredStringExt._

object ColoredStringExt {
  implicit class ColoredString(private val str: String) extends AnyVal {
    def colored(name: String): String = name + str + Console.RESET

    def red: String    = colored(Console.RED)
    def green: String  = colored(Console.GREEN)
    def yellow: String = colored(Console.YELLOW)
  }
}

object Logger {
  def log(submodule: Submodule, msg: String): Unit =
    println(s"${submodule.name} | ".colored(submodule.color) + msg)

  def logGreen(msg: String): Unit  = println(msg.green)
  def logYellow(msg: String): Unit = println(msg.yellow)
  def logRed(msg: String): Unit    = println(msg.red)
}
