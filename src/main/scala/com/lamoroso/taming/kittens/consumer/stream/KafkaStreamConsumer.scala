package com.lamoroso.taming.kittens.consumer.stream

import cats.Applicative
import cats.effect.{ConcurrentEffect, ContextShift, Sync, Timer}
import fs2.kafka._

class KafkaStreamConsumer[F[_]: Applicative: Sync](
    implicit cs: ContextShift[F],
    timer: Timer[F],
    ce: ConcurrentEffect[F],
) extends StreamConsumerAlgebra[F] {

  implicit def messageDeserializer: Deserializer[F, String] =
    Deserializer.lift(bytes => Applicative[F].pure(bytes.dropWhile(_ == 0).toString))

  def stream(group: String, topic: String): fs2.Stream[F, (String, String)] = {
    def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:29092")
        .withGroupId(group)

    consumerStream[F]
      .using(consumerSettings)
      .evalTap(_.subscribeTo(topic))
      .flatMap(_.stream)
      .mapAsync(42)(committable => processRecord(committable.record))
  }

}

object KafkaStreamConsumer {
  def apply[F[_]: Applicative: Sync]()(
      implicit cs: ContextShift[F],
      timer: Timer[F],
      ce: ConcurrentEffect[F],
  ): KafkaStreamConsumer[F] =
    new KafkaStreamConsumer[F]()
}
