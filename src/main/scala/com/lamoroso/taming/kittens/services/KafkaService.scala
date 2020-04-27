package com.lamoroso.taming.kittens.services

import cats.Monad
import cats.effect.{ContextShift, IO, Sync, Timer}
import fs2.kafka._


class KafkaService[F[_]](implicit cs: ContextShift[IO], timer: Timer[IO]) {
  implicit def messageDes: Deserializer[IO, String] =
    Deserializer.lift(bytes => IO.pure(bytes.dropWhile(_ == 0).toString))

  implicit def messageDeserializer(implicit M: Monad[F], f: Sync[F]): Deserializer[F, Array[Byte]] =
    Deserializer.lift(bytes => M.pure(bytes.dropWhile(_ == 0)))


  def stream(group: String, topic: String): fs2.Stream[IO, (String, String)] = {
    def processRecord(record: ConsumerRecord[String, String]): IO[(String, String)] =
      IO.pure(record.key -> record.value)

    val consumerSettings =
      ConsumerSettings[IO, String, String]
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withBootstrapServers("localhost:9092")
        .withGroupId(group)

    consumerStream[IO]
      .using(consumerSettings)
      .evalTap(_.subscribeTo(topic))
      .flatMap(_.stream)
      .mapAsync(42) { committable =>
        processRecord(committable.record)
      }
  }

}

object KafkaService {
  def apply[F[_]]()(implicit cs: ContextShift[IO], timer: Timer[IO]): KafkaService[F] = new KafkaService[F]()
}
