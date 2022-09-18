package spray.json.optics

import monocle.Prism
import spray.json.JsNumber

import scala.util.Try

trait JsNumberOptics {
  import spray.json.DefaultJsonProtocol._
  final lazy val jsNumberBigInt: Prism[JsNumber, BigInt] =
    Prism[JsNumber, BigInt](jn => Try(BigIntJsonFormat.read(jn)).toOption)(JsNumber.apply)

  final lazy val jsNumberInt: Prism[JsNumber, Int] =
    Prism[JsNumber, Int](jn => Try(IntJsonFormat.read(jn)).toOption)(JsNumber.apply)

  final lazy val jsNumberLong: Prism[JsNumber, Long] =
    Prism[JsNumber, Long](jn => Try(LongJsonFormat.read(jn)).toOption)(JsNumber.apply)

  final lazy val jsNumberShort: Prism[JsNumber, Short] =
    Prism[JsNumber, Short](jn => Try(ShortJsonFormat.read(jn)).toOption)(JsNumber.apply(_))

  final lazy val jsNumberBigDecimal =
    Prism[JsNumber, BigDecimal](jn => Try(BigDecimalJsonFormat.read(jn)).toOption)(JsNumber.apply)

  final lazy val jsNumberDouble =
    Prism[JsNumber, Double](jn => Try(DoubleJsonFormat.read(jn)).toOption)(JsNumber.apply(_))

  final lazy val jsNumberByte = Prism[JsNumber, Byte](jn => Try(ByteJsonFormat.read(jn)).toOption)(JsNumber.apply(_))

}

object JsNumberOptics extends JsNumberOptics
