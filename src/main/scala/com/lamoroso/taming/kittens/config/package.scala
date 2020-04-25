package com.lamoroso.taming.kittens

import io.circe.Decoder
import io.circe.generic.semiauto._

package object config {
  implicit val serverDec: Decoder[ServerConfig] = deriveDecoder
  implicit val appDec: Decoder[AppConfig] = deriveDecoder

}
