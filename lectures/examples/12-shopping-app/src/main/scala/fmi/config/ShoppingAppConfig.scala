package fmi.config

import fmi.infrastructure.db.DbConfig
import io.circe.Codec
import com.typesafe.config.Config

case class ShoppingAppConfig(
  secretKey: String,
  http: HttpConfig,
  database: DbConfig
)

object ShoppingAppConfig:
  def fromConfig(config: Config): ShoppingAppConfig =
    val secretKey = config.getString("secretKey")

    val http = HttpConfig(
      config.getInt("http.port")
    )

    val dbConfig = config.getConfig("database")
    val database = DbConfig(
      dbConfig.getString("host"),
      dbConfig.getInt("port"),
      dbConfig.getString("user"),
      dbConfig.getString("password"),
      dbConfig.getString("name"),
      dbConfig.getString("schema"),
      dbConfig.getInt("connectionPoolSize")
    )

    ShoppingAppConfig(
      secretKey,
      http,
      database
    )
