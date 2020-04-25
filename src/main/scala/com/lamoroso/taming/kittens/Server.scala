package com.lamoroso.taming.kittens

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.implicits._
import com.lamoroso.taming.kittens.config._
import com.lamoroso.taming.kittens.config.AppConfig
import com.lamoroso.taming.kittens.routes.HelloWordRoute
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}

object Server extends IOApp {
  def createServer[F[_] : ContextShift : ConcurrentEffect : Timer]: Resource[F, H4Server[F]] = for {
    conf <- Resource.liftF(parser.decodePathF[F, AppConfig]("app"))
    httpApp = Router(
      "/hello" -> HelloWordRoute()
    ).orNotFound
    server <- BlazeServerBuilder[F]
      .bindHttp(conf.server.port, conf.server.host)
      .withHttpApp(httpApp)
      .resource
  } yield server

  override def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)
}
