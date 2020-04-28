package com.lamoroso.taming.kittens.consumer.stream

import cats.Applicative
import cats.effect.{ConcurrentEffect, ContextShift, Sync, Timer}
import fs2.kafka._

class KafkaStreamConsumer[F[_]: Applicative: Sync](bootstrapServers: String)(
    implicit cs: ContextShift[F],
    timer: Timer[F],
    ce: ConcurrentEffect[F],
) extends StreamConsumerAlgebra[F] {

  private implicit def messageDeserializer: Deserializer[F, String] =
    Deserializer.lift(bytes => Applicative[F].pure(new String(bytes)))

  override def stream(group: String, topic: String): fs2.Stream[F, (String, String)] =
    consumerStream[F]
      .using(consumerSettings(group, topic))
      .evalTap(_.subscribeTo(topic))
      .flatMap(_.stream)
      .mapAsync(42)(committable => processRecord(committable.record))

  override def committableStream(group: String, topic: String): fs2.Stream[F, (String, String)] =
    ???
//    consumerStream[F]
//      .using(consumerSettings(group, topic))
//      .evalTap(_.subscribeTo(topic))
//      .flatMap(_.stream)
//      .mapAsync(42)(committable => processRecord(committable.record).as(committable.offset))
//      .through(
//        commitBatchWithin(500, FiniteDuration(15, TimeUnit.SECONDS)),
//      ) //commits once every 500 offsets or 15 seconds, whichever happens first.

  private def consumerSettings(group: String, topic: String): ConsumerSettings[F, String, String] =
    ConsumerSettings[F, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withBootstrapServers(bootstrapServers)
      .withGroupId(s"$group-$topic")

  private def processRecord(record: ConsumerRecord[String, String]): F[(String, String)] =
    Applicative[F].pure(record.key -> record.value)

}

object KafkaStreamConsumer {
  def apply[F[_]](bootstrapServers: String)(
      implicit cs: ContextShift[F],
      timer: Timer[F],
      ce: ConcurrentEffect[F],
  ): KafkaStreamConsumer[F] =
    new KafkaStreamConsumer[F](bootstrapServers)
}
