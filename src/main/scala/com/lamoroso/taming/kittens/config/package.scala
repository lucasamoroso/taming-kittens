package com.lamoroso.taming.kittens

import io.circe.Decoder
import io.circe.generic.semiauto._

package object config {
  implicit lazy val serverDec: Decoder[ServerConfig] = deriveDecoder[ServerConfig]
  implicit lazy val kafkaDec: Decoder[KafkaConfig] = deriveDecoder[KafkaConfig]
  implicit lazy val appDec: Decoder[AppConfig] = deriveDecoder[AppConfig]

}
