import $ivy.`com.lihaoyi::os-lib-watch:0.4.2`
import os.Path

@main
def main(script: Path = os.pwd / "demo.kts"): Unit = {
  val classpath = os.proc("cs", "fetch", "ocs-app:latest.release", "-p").call()
  val jarName   = s"${script.baseName}.jar"
  val kotlinc = Seq(
    "kotlinc",
    "-jvm-target",
    "1.8",
    "-Xuse-experimental=kotlin.time.ExperimentalTime",
    "-classpath",
    classpath.out.text(),
    script.toString(),
    "-d",
    jarName
  )
  val run =
    Seq(
      "cs",
      "launch",
      "--extra-jars",
      jarName,
      "--java-opt",
      "-Dscripts.ESW.Darknight.scriptClass=Demo",
      "ocs-app:latest.release",
      "--",
      "sequencer",
      "-s",
      "ESW",
      "-m",
      "Darknight"
    )

  os.proc(kotlinc).call(stdout = os.Inherit)
  var process = os.proc(run).spawn(stdout = os.Inherit)

  os.watch.watch(
    Seq(script),
    paths => {
      println("paths changed: " + paths.mkString(", "))
      process.destroyForcibly()
      process.waitFor()
      Thread.sleep(10000)
      os.proc(kotlinc).call(stdout = os.Inherit)
      process = os.proc(run).spawn(stdout = os.Inherit)
    }
  )
  Thread.sleep(100000)
}
