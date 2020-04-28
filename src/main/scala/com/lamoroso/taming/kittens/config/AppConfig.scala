package com.lamoroso.taming.kittens.config

final case class ServerConfig(host: String, port: Int)

final case class KafkaConfig(bootstrapServers: String)

final case class AppConfig(server: ServerConfig, kafka: KafkaConfig)
