package spray.json.optics

import org.scalatest.flatspec.AnyFlatSpec
import monocle.law.discipline
import all._
import monocle.Optional
import syntax._
import spray.json._

import java.math.BigInteger

class Test extends AnyFlatSpec {

  private def fromUnscaledAndScale(unscaled: BigInteger, scale: Long): (BigInteger, BigInteger) =
    if (unscaled == BigInteger.ZERO) (BigInteger.ZERO, BigInteger.ZERO)
    else {
      var current = unscaled
      var depth = scale

      var divAndRem = current.divideAndRemainder(BigInteger.TEN)

      while (divAndRem(1) == BigInteger.ZERO) {
        current = divAndRem(0)
        depth -= 1L
        divAndRem = current.divideAndRemainder(BigInteger.TEN)
      }

      (current, BigInteger.valueOf(depth))
    }

  def toBigIntegerWithMaxDigits(unscaled: BigInteger, scale: BigInteger, maxDigits: BigInteger): Option[BigInteger] =
    if (!(scale.signum < 1)) None
    else {
      val digits = BigInteger.valueOf(unscaled.abs.toString.length.toLong).subtract(scale)

      if (digits.compareTo(maxDigits) > 0) None
      else
        Some(new java.math.BigDecimal(unscaled, scale.intValue).toBigInteger)
    }

  private val MaxBigIntegerDigits: BigInteger = BigInteger.valueOf(1L << 18)

  it should "works" in {
    val json = """{ 
      |"x": 1,
      |"y" : {
      |   "z" : 2
      |   },
      |"conditions": [
      | { "property": "a" },
      | { "property": "b" },
      | { "p": 1 }
      |]
      | }""".stripMargin.parseJson
//    "y".andThen("z": Optional[JsValue, JsValue])

    val big: BigDecimal = 5.0194364116707635e-183
    val jsn = JsNumber(5.0194364116707635e-183)

    val x: JsValue = JsNumber(0.2423742399818780)

    val really = jsBigInt.getOrModify(jsn).fold(identity, jsBigInt.reverseGet)

    println(jsBigInt.getOption(jsn))

    val unscaled = big.bigDecimal.unscaledValue()
    val scale = big.bigDecimal.scale().toLong

    val (sig, exp) = fromUnscaledAndScale(unscaled, scale)

    val r = toBigIntegerWithMaxDigits(sig, exp, MaxBigIntegerDigits).map(BigInt(_))

    assert(true)
  }
}
