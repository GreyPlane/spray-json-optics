package spray.json.optics

import cats.implicits._
import monocle.law.discipline.function.{ AtTests, EachTests, FilterIndexTests, IndexTests }
import monocle.law.discipline.{ PrismTests, TraversalTests }
import org.scalacheck.Arbitrary._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline
import spray.json._
import spray.json.testing.ArbitraryInstances._
import spray.json.testing.CogenInstances._
import spray.json.testing.EqInstances._

class OpticsSuite extends AnyFunSuite with FunSuiteDiscipline with Checkers {

  checkAll("JsValue to Unit", PrismTests(jsNull))
  checkAll("JsValue to String", PrismTests(jsString))
  checkAll("JsValue to JsNumber", PrismTests(jsNumber))
  // TODO it's ridiculously complicate to get those numbers right
  // need impl mechanism to check if a js number is safe for those types
//  checkAll("JsValue to BigInt", PrismTests(jsBigInt))
  checkAll("JsValue to BigDecimal", PrismTests(jsBigDecimal))
  checkAll("JsValue to Int", PrismTests(jsInt))
  checkAll("JsValue to Long", PrismTests(jsLong))
  checkAll("JsValue to Short", PrismTests(jsShort))
//  checkAll("JsValue to Double", PrismTests(jsDouble))
  checkAll("JsValue to Byte", PrismTests(jsByte))
  checkAll("JsObject at", AtTests[JsObject, String, Option[JsValue]])
  checkAll("JsObject index", IndexTests[JsObject, String, JsValue])
  checkAll("JsObject filterIndex", FilterIndexTests[JsObject, String, JsValue])
  checkAll("JsValue each", EachTests[JsValue, JsValue])
  checkAll("Js descendants traversal", TraversalTests[JsValue, JsValue](jsDescendants))
  checkAll("Js plated traversal", TraversalTests[JsValue, JsValue](jsPlated.plate))
}
