package spray.json.optics

import monocle.Prism
import spray.json.{JsValue, JsonFormat}

import scala.util.{Failure, Success, Try}

private object UnsafeOptics {
  def parse[A](implicit format: JsonFormat[A]): Prism[JsValue, A] =
    Prism[JsValue, A](jsv =>
      Try(format.read(jsv)) match {
        case Success(value) => Some(value)
        case Failure(_)     => None
      }
    )(format.write)

  final private val keyMissingNone: Option[None.type] = Some(None)

  def optionParse[A](implicit format: JsonFormat[A]): Prism[Option[JsValue], Option[A]] =
    Prism[Option[JsValue], Option[A]] {
      case Some(jsv) =>
        Try(format.read(jsv)) match {
          case Success(value) => Some(Some(value))
          case Failure(_)     => None
        }
      case None => keyMissingNone
    }(_.map(format.write))
}
