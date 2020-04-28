package com.lamoroso.taming.kittens.routes

import cats.effect.Sync
import com.lamoroso.taming.kittens.consumer.stream.StreamConsumerAlgebra
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ConsumerRoutes[F[_]: Sync](streamConsumer: StreamConsumerAlgebra[F]) extends Http4sDsl[F] {

  def routes(): HttpRoutes[F] =
    streamRoute()

  private def streamRoute(): HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / group / topic =>
      Ok(streamConsumer.stream(group, topic).map(_._2))
    case req @ GET -> Root / "committeable" / group / topic =>
      Ok(streamConsumer.committableStream(group, topic))
  }

}

object ConsumerRoutes {
  def apply[F[_]: Sync](streamConsumer: StreamConsumerAlgebra[F]): HttpRoutes[F] =
    new ConsumerRoutes[F](streamConsumer).routes()
}
