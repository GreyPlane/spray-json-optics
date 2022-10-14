package spray.json.optics

import cats.Applicative
import spray.json.{JsObject, JsValue}
import cats.implicits._

private[optics] object utils {
  implicit class RichJsObject(val jso: JsObject) extends AnyVal {
    def traverseMapFields[F[_]](f: ((String, JsValue)) => F[JsValue])(implicit A: Applicative[F]): F[JsObject] =
      jso.fields.toList.traverse { field => f(field).map(field._1 -> _) }.map(fs => jso.copy(fields = fs.toMap))
  }
}
