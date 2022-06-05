package fmi

import cats.effect.kernel.Resource
import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import com.typesafe.config.ConfigFactory
import fmi.config.ShoppingAppConfig
import fmi.infrastructure.CryptoService
import fmi.infrastructure.db.DbModule
import fmi.inventory.InventoryModule
import fmi.shopping.ShoppingModule
import fmi.user.UsersModule
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Server

object ShoppingApp extends IOApp.Simple:
  val app: Resource[IO, Server] = for
    config <- Resource
      .eval(IO.blocking(ConfigFactory.load()))
      .map(_.getConfig("shoppingApp"))
      .map(ShoppingAppConfig.fromConfig)

    cryptoService = new CryptoService(config.secretKey)

    dbModule <- DbModule(config.database)

    usersModule <- UsersModule(dbModule.dbTransactor, cryptoService)
    inventoryModule <- InventoryModule(dbModule.dbTransactor)
    shoppingModule <- ShoppingModule(dbModule.dbTransactor, inventoryModule.productStockDao)

    nonAuthenticatedRoutes = usersModule.routes <+> inventoryModule.routes
    authenticatedRoutes = usersModule.authMiddleware {
      usersModule.authenticatedRoutes <+> inventoryModule.authenticatedRoutes <+> shoppingModule.authenticatedRoutes
    }

    routes = (nonAuthenticatedRoutes <+> authenticatedRoutes).orNotFound

    httpServer <- BlazeServerBuilder[IO]
      .bindHttp(config.http.port, "localhost")
      .withHttpApp(routes)
      .resource
  yield httpServer

  def run: IO[Unit] =
    app
      .use(_ => IO.never)
      .onCancel(IO.println("Bye, see you again \uD83D\uDE0A"))
