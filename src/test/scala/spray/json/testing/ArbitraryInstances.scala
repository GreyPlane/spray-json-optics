package spray.json.testing

import org.scalacheck.{ Arbitrary, Gen }
import spray.json.{ JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue }

object ArbitraryInstances {

  /**
   * The maximum depth of a generated JSON value.
   */
  protected def maxJsonDepth: Int = 5

  /**
   * The maximum number of values in a generated JSON array.
   */
  protected def maxJsonArraySize: Int = 10

  /**
   * The maximum number of key-value pairs in a generated JSON object.
   */
  protected def maxJsonObjectSize: Int = 10

  implicit val arbitraryJsNumber: Arbitrary[JsNumber] = Arbitrary(
    Gen.oneOf(
      Arbitrary.arbitrary[BigDecimal].map[JsNumber](JsNumber.apply),
      Arbitrary.arbitrary[BigInt].map[JsNumber](JsNumber.apply),
      Arbitrary.arbitrary[Long].map[JsNumber](JsNumber.apply),
      Arbitrary.arbitrary[Double].map[JsNumber](JsNumber.apply(_)),
      Arbitrary.arbitrary[Float].map[JsNumber](JsNumber.apply(_))))

  val genJsNull: Gen[JsValue] = Gen.const(JsNull)
  val genJsBoolean: Gen[JsValue] = Arbitrary.arbitrary[Boolean].map(JsBoolean.apply)
  val genJsString: Gen[JsValue] = Arbitrary.arbitrary[String].map(JsString.apply)
  val genJsNumber: Gen[JsValue] = Arbitrary.arbitrary[JsNumber]
  def genJsArray(depth: Int): Gen[JsValue] =
    Gen
      .choose(0, maxJsonArraySize)
      .flatMap(size => Gen.listOfN(size, genJsValueAtDepth(depth + 1)).map(xs => JsArray.apply(xs.toVector)))
  def genJsObject(depth: Int): Gen[JsObject] =
    Gen
      .choose(0, maxJsonObjectSize)
      .flatMap(size => {
        val genField = Arbitrary.arbitrary[String].flatMap(key => genJsValueAtDepth(depth + 1).map(key -> _))
        Gen.mapOfN(size, genField).map(JsObject.apply)
      })
  private[this] def genJsValueAtDepth(depth: Int): Gen[JsValue] = {
    val genJsValues = List(genJsNumber, genJsString) ++ (
        if (depth < maxJsonDepth) List(genJsArray(depth), genJsObject(depth)) else Nil
      )
    Gen.oneOf(genJsBoolean, genJsNull, genJsValues: _*)
  }

  implicit val arbitraryJsValue: Arbitrary[JsValue] = Arbitrary(genJsValueAtDepth(0))
  implicit val arbitraryJsObject: Arbitrary[JsObject] = Arbitrary(genJsObject(0))
}
