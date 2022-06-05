package fmi.inventory
import cats.effect.IO
import cats.syntax.all.*
import fmi.user.{AuthenticatedUser, UserRole}
import fmi.utils.CirceUtils
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.dsl.io.*
import org.http4s.{AuthedRoutes, HttpRoutes}

class InventoryRouter(productDao: ProductDao, productStockDao: ProductStockDao):
  import InventoryJsonCodecs.given
  import org.http4s.circe.CirceEntityCodec.given

  def nonAuthenticatedRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "stock" =>
      Ok(productStockDao.retrieveAllAvailableStock)

    case GET -> Root / "products" / sku =>
      val productSku = ProductSku(sku)

      productDao
        .retrieveProduct(productSku)
        .flatMap(
          _.fold(NotFound())(Ok(_))
        )
  }

  def authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO] = AuthedRoutes.of[AuthenticatedUser, IO] {
    case authReq @ POST -> Root / "products" as user if user.role == UserRole.Admin =>
      Ok(authReq.req.as[Product] >>= productDao.addProduct)

    case authReq @ POST -> Root / "stock" as user if user.role == UserRole.Admin =>
      val adjustmentResult = authReq.req.as[InventoryAdjustment] >>= productStockDao.applyInventoryAdjustment

      adjustmentResult.flatMap {
        case SuccessfulAdjustment => Ok()
        case NotEnoughStockAvailable => Conflict()
      }
  }

object InventoryJsonCodecs:
  given Codec[ProductSku] = CirceUtils.unwrappedCodec(ProductSku.apply)(_.sku)
  given Codec[Product] = deriveCodec
  given Codec[ProductStock] = deriveCodec

  given Codec[ProductStockAdjustment] = deriveCodec
  given Codec[InventoryAdjustment] = deriveCodec
