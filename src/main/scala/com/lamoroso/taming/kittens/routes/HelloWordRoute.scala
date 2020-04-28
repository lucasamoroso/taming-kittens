package com.lamoroso.taming.kittens.routes

import cats.effect.Sync
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

class HelloWordRoute[F[_]: Sync] extends Http4sDsl[F] {

  implicit val stringDec: EntityDecoder[F, String] = jsonOf

  def routes(): HttpRoutes[F] =
    helloWordRoute()

  private def helloWordRoute(): HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root => Ok("Hello word")
  }

}

object HelloWordRoute {
  def apply[F[_]: Sync](): HttpRoutes[F] =
    new HelloWordRoute[F]().routes()
}
