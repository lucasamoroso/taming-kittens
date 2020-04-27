package com.lamoroso.taming.kittens.services

import cats.Applicative
import cats.effect.{ConcurrentEffect, ContextShift, IO, Sync, Timer}
import fs2.kafka._

class KafkaService[F[_]: Applicative: Sync](
    implicit cs: ContextShift[F],
    timer: Timer[F],
    ce: ConcurrentEffect[F],
) {

  implicit def messageDeserializer: Deserializer[F, Array[Byte]] =
    Deserializer.lift(bytes => Applicative[F].pure(bytes.dropWhile(_ == 0).toString))

  def stream(group: String, topic: String): fs2.Stream[F, (String, String)] = {
    def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
      Applicative[F].pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[F, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId(group)

    consumerStream[F]
      .using(consumerSettings)
      .evalTap(_.subscribeTo(topic))
      .flatMap(_.stream)
      .mapAsync(42)(committable => processRecord(committable.record))
  }

}

object KafkaService {
  def apply[F[_]: Applicative: Sync]()(implicit cs: ContextShift[F], timer: Timer[F],  ce: ConcurrentEffect[F]): KafkaService[F] =
    new KafkaService[F]()
}
