package spray.json.testing

import cats.implicits._
import cats.kernel.Eq
import spray.json._

object EqInstances {
  private def arrayJsValueEq(x: Vector[JsValue], y: Vector[JsValue]): Boolean = {
    val it0 = x.iterator
    val it1 = y.iterator
    while (it0.hasNext && it1.hasNext) {
      if (jsValueEq.neqv(it0.next(), it1.next())) return false
    }
    it0.hasNext == it1.hasNext
  }

  implicit def jsNumberEq: Eq[JsNumber] = Eq.instance((x, y) => x.value == y.value)

//  implicit def jsObjectFieldsEq: Eq[Map[String, JsValue]] = implicitly[Eq[Map[String, JsValue]]]

  implicit def jsValueEq: Eq[JsValue] = Eq.instance {
    case (xs: JsObject, ys: JsObject) => xs === ys
    case (JsArray(xs), JsArray(ys))   => arrayJsValueEq(xs, ys)
    case (JsNumber(x), JsNumber(y))   => x == y
    case (JsString(x), JsString(y))   => x == y
    case (JsBoolean(x), JsBoolean(y)) => x == y
    case (JsNull, JsNull)             => true
  }

  implicit def jsObjectEq: Eq[JsObject] = Eq.instance((x, y) => x.fields.eqv(y.fields))

}
