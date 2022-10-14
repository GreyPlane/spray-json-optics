package spray.json.testing

import org.scalacheck.Cogen
import spray.json.{JsNumber, JsObject, JsValue}

object CogenInstances {
  implicit val jsValueCogen: Cogen[JsValue] = Cogen(_.hashCode())
  implicit val jsObjectCogen: Cogen[JsObject] = Cogen(_.hashCode())
  implicit val jsNumberCogen: Cogen[JsNumber] = Cogen(_.hashCode())
}
