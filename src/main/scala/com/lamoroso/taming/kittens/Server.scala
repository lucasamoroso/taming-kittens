package com.lamoroso.taming.kittens

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.implicits._
import com.lamoroso.taming.kittens.config.{AppConfig, _}
import com.lamoroso.taming.kittens.consumer.stream.KafkaStreamConsumer
import com.lamoroso.taming.kittens.routes.{ConsumerRoutes, HelloWordRoute}
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}

object Server extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    createServer.use(_ => IO.never).as(ExitCode.Success)

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      conf <- Resource.liftF(parser.decodePathF[F, AppConfig]("app"))
      streamConsumer = KafkaStreamConsumer[F](conf.kafka.bootstrapServers)
      httpApp = Router(
        "/hello" -> HelloWordRoute(),
        "/stream" -> ConsumerRoutes[F](streamConsumer),
      ).orNotFound
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server
}
