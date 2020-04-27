/*
package com.lamoroso.taming.kittens.routes

import cats.effect.Sync
import com.lamoroso.taming.kittens.services.KafkaService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ConsumerRoutes[F[_] : Sync](kafkaService: KafkaService[F]) extends Http4sDsl[F] {

  private def streamRoute(): HttpRoutes[F] = HttpRoutes.of[F] {
    case req@GET -> Root / group / topic =>
       kafkaService.stream(group, topic).pull
  }

  def routes(): HttpRoutes[F] =
    streamRoute()

}

object ConsumerRoutes {
  def apply[F[_] : Sync](kafkaService: KafkaService[F]): HttpRoutes[F] =
    new ConsumerRoutes[F](kafkaService).routes()
}
*/
