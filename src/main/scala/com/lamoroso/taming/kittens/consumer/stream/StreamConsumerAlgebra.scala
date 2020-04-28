package com.lamoroso.taming.kittens.consumer.stream

trait StreamConsumerAlgebra[F[_]] {
  def stream(group: String, topic: String): fs2.Stream[F, (String, String)]
  def committableStream(group: String, topic: String): fs2.Stream[F, (String, String)]
}
