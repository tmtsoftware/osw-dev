package dev.apps

import dev.models.Submodule.{CSW, ESW}
import dev.utils.Sbt

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object TMTRunner extends App {
  val cswServices = Sbt.run(CSW, "Server online at", "csw-services/run start -c")
  val eswServices = Sbt.run(ESW, "Server online at", "esw-services/run start-eng-ui-services")

  // if any of the process exits, then kill other
  val f1 = Future {
    cswServices.join()
    eswServices.destroyForcibly()
  }

  val f2 = Future {
    eswServices.join()
    cswServices.destroyForcibly()
  }

  Await.result(f2, Duration.Inf)
  Await.result(f1, Duration.Inf)
}
