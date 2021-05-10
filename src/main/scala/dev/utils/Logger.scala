package dev.utils

object Logger {
  def log(prefixColor: String, prefix: String, msg: String): Unit =
    println(s"$prefixColor$prefix | ${Console.RESET}$msg")

  def logGreen(msg: String): Unit = println(s"${Console.GREEN}$msg${Console.RESET}")
}
