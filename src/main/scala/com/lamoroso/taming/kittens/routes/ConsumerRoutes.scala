
package com.lamoroso.taming.kittens.routes

import cats.effect.Sync
import com.lamoroso.taming.kittens.consumer.stream.StreamConsumerAlgebra
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ConsumerRoutes[F[_] : Sync](streamConsumer: StreamConsumerAlgebra[F]) extends Http4sDsl[F] {

  private def streamRoute(): HttpRoutes[F] = HttpRoutes.of[F] {
    case req@GET -> Root / group / topic =>
      // streamConsumer.stream(group, topic).pull
    Ok("To implement")
  }


  def routes(): HttpRoutes[F] =
    streamRoute()

}

object ConsumerRoutes {
  def apply[F[_] : Sync](streamConsumer: StreamConsumerAlgebra[F]): HttpRoutes[F] =
    new ConsumerRoutes[F](streamConsumer).routes()
}

