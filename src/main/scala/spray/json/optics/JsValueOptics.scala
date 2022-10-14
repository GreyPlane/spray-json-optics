package spray.json.optics

import cats.Applicative
import cats.implicits._
import monocle._
import monocle.function.{At, Each, Plated}
import spray.json._
import spray.json.optics.utils._

import scala.util.Try

trait JsValueOptics {

  final lazy val jsNull = Prism[JsValue, Unit] {
    case JsNull => Some()
    case _      => None
  }(_ => JsNull)
  final lazy val jsBoolean = Prism[JsValue, Boolean](jsv => Try(BooleanJsonFormat.read(jsv)).toOption)(JsBoolean.apply)
  final lazy val jsNumber = Prism[JsValue, JsNumber] {
    case jsn: JsNumber => Some(jsn)
    case _             => None
  }(identity)
  final lazy val jsInt: Prism[JsValue, Int] = jsNumber.andThen(jsNumberInt)
  final lazy val jsLong: Prism[JsValue, Long] = jsNumber.andThen(jsNumberLong)
  final lazy val jsShort: Prism[JsValue, Short] = jsNumber.andThen(jsNumberShort)
  final lazy val jsBigInt: Prism[JsValue, BigInt] = jsNumber.andThen(jsNumberBigInt)
  final lazy val jsBigDecimal: Prism[JsValue, BigDecimal] = jsNumber.andThen(jsNumberBigDecimal)
  final lazy val jsByte: Prism[JsValue, Byte] = jsNumber.andThen(jsNumberByte)
  final lazy val jsDouble: Prism[JsValue, Double] = jsNumber.andThen(jsNumberDouble)
  final lazy val jsString: Prism[JsValue, String] =
    Prism[JsValue, String](str => Try(StringJsonFormat.read(str)).toOption)(JsString.apply)

  final lazy val jsArray: Prism[JsValue, Vector[JsValue]] = Prism[JsValue, Vector[JsValue]] {
    case JsArray(values) => Some(values)
    case _               => None
  }(JsArray.apply(_))

  final lazy val jsObject: Prism[JsValue, JsObject] =
    Prism[JsValue, JsObject](jsv => Try(jsv.asJsObject).toOption)(_.toJson)

  final lazy val jsDescendants: Traversal[JsValue, JsValue] = new Traversal[JsValue, JsValue] {
    def modifyA[F[_]](f: JsValue => F[JsValue])(s: JsValue)(implicit F: Applicative[F]): F[JsValue] = s match {
      case jso: JsObject   => F.map(Each.each[JsObject, JsValue].modifyA(f)(jso))(identity)
      case JsArray(values) => F.map(Each.each[Vector[JsValue], JsValue].modifyA(f)(values))(JsArray.apply(_))
      case _               => F.pure(s)
    }
  }

  final def parse[T](implicit format: JsonFormat[T]): Prism[JsValue, T] = UnsafeOptics.parse[T]

  final def select[T](
    field: String
  )(implicit format: JsonFormat[T], A: At[JsObject, String, Option[JsValue]]): Optional[JsValue, Option[T]] =
    jsObject.andThen(A.at(field).andThen(UnsafeOptics.optionParse[T]))

  implicit final lazy val jsValueEach: Each[JsValue, JsValue] = Each(jsDescendants)

  implicit final lazy val jsPlated: Plated[JsValue] = new Plated[JsValue] {
    def plate: Traversal[JsValue, JsValue] = new Traversal[JsValue, JsValue] {
      def modifyA[F[_]](f: JsValue => F[JsValue])(s: JsValue)(implicit F: Applicative[F]): F[JsValue] = s match {
        case jso: JsObject      => jso.traverseMapFields(f.compose(_._2)).map(identity)
        case JsArray(elements)  => elements.traverse(f).map(JsArray.apply(_))
        case JsString(value)    => value.pure[F].map(JsString.apply)
        case JsNumber(value)    => value.pure[F].map(JsNumber.apply)
        case boolean: JsBoolean => boolean.pure[F].map(identity)
        case JsNull             => F.pure(s)
      }
    }
  }

}

object JsValueOptics extends JsValueOptics
