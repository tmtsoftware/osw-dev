import pprint.PPrinter

val wd = os.pwd
val csw = wd / "csw"
val esw = wd / "esw"
val sbt = "/Users/pritamkadam/Library/Application Support/Coursier/bin/sbt"

def log(prefixColor: String, prefix: String, msg: String): Unit =
  println(s"$prefixColor$prefix | ${Console.RESET}$msg")

val green = PPrinter(colorLiteral = fansi.Color.Green)

def runSbt(cwd: os.Path, waitForOutput: String, prefixColor: String, prefix: String, cmd: String*) = {
  green.pprintln(s"Running ${cmd.mkString(" ")}")
  @volatile var finished = false
  val process = os.proc(sbt, cmd).spawn(cwd = cwd, stdin = o1s.Inherit, stderr = os.Inherit)
  sys.addShutdownHook(process.destroyForcibly())

  while (!finished && (process.stdout.available() > 0 || process.isAlive())) {
    val line = process.stdout.readLine()
    log(prefixColor, prefix, line)
    if (line.contains(waitForOutput)) finished = true
  }

  green.pprintln(s"Started ${cmd.mkString(" ")}")
  process
}

val cswServices = runSbt(csw, "Server online at", Console.YELLOW, "CSW-SERVICES", "csw-services/run start -c")
val eswServices = runSbt(esw, "Server online at", Console.MAGENTA, "ESW-SERVICES", "esw-services/run start-eng-ui-services")

cswServices.join()
eswServices.join()