package spray.json.optics

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import spray.json._

class BasicUsageSuite extends AnyFlatSpec with should.Matchers {

  behavior.of("JsonOptics")

  it should "parse nested fields to wanted type" in {
    val json = """{ 
      |"conditions": [
      | { "property": "a" },
      | { "property": "b" }
      |]
      | }""".stripMargin.parseJson

    val conditions = jsObject.index("conditions")
    val properties = conditions.andThen(jsArray).each.andThen(jsObject).index("property").andThen(parse[String])

    properties.getAll(json) shouldBe List("a", "b")

  }

  it should "select fields that doesn't exist as Some(None)" in {
    val json = """{
      | "a": 1,
      | "b": 2
      |}""".stripMargin.parseJson

    val a = select[String]("a") // None
    val b = select[Int]("b")
    val c = select[Int]("c") // Some(None)

    a.getOption(json) shouldBe None
    b.getOption(json) shouldBe Some(Some(2))
    c.getOption(json) shouldBe Some(None)
  }

}
