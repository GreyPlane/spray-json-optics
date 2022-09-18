package spray.json.optics

import monocle.function.{ At, Index }
import monocle.{ Fold, Lens, Optional, Traversal }
import JsValueOptics.{ jsDescendants, jsObject }
import JsObjectOptics._
import spray.json.{ JsObject, JsValue, JsonFormat }

import scala.language.implicitConversions

object syntax {

  implicit class RichString(val string: String) extends AnyVal {
    def index(implicit I: Index[JsObject, String, JsValue]): Optional[JsValue, JsValue] =
      jsObject.andThen(I.index(string))
  }

//  implicit class RichLens[A](val lhs: Lens[A, JsValue]) extends AnyVal {
//    def at(field: String): Optional[A, Option[JsValue]] = lhs.andThen(jsObject).at(field)
//    def as[T](implicit jsonFormat: JsonFormat[T]): Optional[A, T] = lhs.andThen(UnsafeOptics.parse)
//    def atAs[T](field: String)(implicit jsonFormat: JsonFormat[T]): Optional[A, Option[T]] =
//      at(field).andThen(UnsafeOptics.optionParse)
//  }

  implicit class RichOptional[A](val lhs: Optional[A, JsValue]) extends AnyVal {
    def field(name: String): Optional[A, Option[JsValue]] = lhs.andThen(jsObject).at(name)
    def convert[T](implicit jsonFormat: JsonFormat[T]): Optional[A, T] = lhs.andThen(UnsafeOptics.parse)
    def convertField[T](name: String)(implicit jsonFormat: JsonFormat[T]): Optional[A, Option[T]] =
      field(name).andThen(UnsafeOptics.optionParse)
  }

  implicit class RichTraversal[A](val lhs: Traversal[A, JsValue]) extends AnyVal {
    def field(name: String): Traversal[A, Option[JsValue]] = lhs.andThen(jsObject).at(name)
    def convert[T](implicit jsonFormat: JsonFormat[T]): Traversal[A, T] = lhs.andThen(UnsafeOptics.parse)
    def convertField[T](name: String)(implicit jsonFormat: JsonFormat[T]): Traversal[A, Option[T]] =
      field(name).andThen(UnsafeOptics.optionParse)
  }

  implicit class RichFold[A](val lhs: Fold[A, JsValue]) extends AnyVal {
    def field(name: String): Fold[A, Option[JsValue]] = lhs.andThen(jsObject).at(name)
    def convert[T](implicit jsonFormat: JsonFormat[T]): Fold[A, T] = lhs.andThen(UnsafeOptics.parse)
    def convertField[T](name: String)(implicit jsonFormat: JsonFormat[T]): Fold[A, Option[T]] =
      field(name).andThen(UnsafeOptics.optionParse)
  }

  import scala.language.implicitConversions

  implicit def string2field(str: String): Optional[JsValue, JsValue] = jsObject.index(str)
}
