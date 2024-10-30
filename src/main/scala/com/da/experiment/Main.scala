package com.da.experiment

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.http.scaladsl.Http
import sttp.tapir.server.pekkohttp.PekkoHttpServerInterpreter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object Main {

  def main(args: Array[String]): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem()

    val route = PekkoHttpServerInterpreter().toRoute(Endpoints.all)

    val port = sys.env.get("http.port").map(_.toInt).getOrElse(8080)

    val bindingFuture = Http()
      .newServerAt("localhost", port)
      .bindFlow(route)
      .map { binding =>
        println(s"Go to http://localhost:${binding.localAddress.getPort}/docs to open SwaggerUI. Press ENTER key to exit.")
        binding
      }

    StdIn.readLine()

    bindingFuture.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
  }
}
