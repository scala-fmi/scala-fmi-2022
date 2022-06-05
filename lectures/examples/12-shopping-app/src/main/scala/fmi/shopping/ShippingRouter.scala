package fmi.shopping

import cats.effect.IO
import cats.syntax.all.*
import fmi.inventory.ProductSku
import fmi.user.{AuthenticatedUser, UserId}
import fmi.utils.CirceUtils
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.AuthedRoutes
import org.http4s.dsl.io.*

class ShippingRouter(orderService: OrderService):
  import OrderJsonCodecs.given
  import org.http4s.circe.CirceEntityCodec.given

  def authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO] = AuthedRoutes.of[AuthenticatedUser, IO] {
    case authReq @ POST -> Root / "orders" as user =>
      val placedOrder = authReq.req.as[ShoppingCart] >>= (orderService.placeOrder(user.id, _))

      placedOrder.flatMap {
        _.fold(_ => Conflict(), Ok(_))
      }
  }

object OrderJsonCodecs:
  import fmi.inventory.InventoryJsonCodecs.given Codec[ProductSku]
  import fmi.user.UsersJsonCodecs.given Codec[UserId]

  given Codec[OrderLine] = deriveCodec
  given Codec[ShoppingCart] = deriveCodec

  given Codec[OrderId] = CirceUtils.unwrappedCodec(OrderId.apply)(_.id)
  given Codec[Order] = deriveCodec
