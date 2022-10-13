package spray.json.optics

import cats.implicits._
import cats.{ Applicative, Monoid }
import monocle.function.{ At, Each, FilterIndex, Index }
import monocle.{ Fold, Lens, Traversal }
import spray.json.{ JsObject, JsValue }
import utils._

trait JsObjectOptics {

  implicit final lazy val jsObjectFold: Fold[JsObject, (String, JsValue)] = new Fold[JsObject, (String, JsValue)] {
    def foldMap[M](f: ((String, JsValue)) => M)(obj: JsObject)(implicit evidence$1: Monoid[M]): M =
      obj.fields.toList.foldMap(f)
  }

  private final lazy val jsObjectAtLens = (field: String) =>
    Lens[JsObject, Option[JsValue]](_.fields.get(field))(optVal =>
      jso => jso.copy(fields = optVal.fold(jso.fields.removed(field))(jso.fields.updated(field, _))))

  implicit final lazy val jsObjectAt: At[JsObject, String, Option[JsValue]] =
    (field: String) => jsObjectAtLens(field)

  implicit final lazy val jsObjectEach: Each[JsObject, JsValue] = new Each[JsObject, JsValue] {
    def each: Traversal[JsObject, JsValue] = new Traversal[JsObject, JsValue] {
      def modifyA[F[_]](f: JsValue => F[JsValue])(from: JsObject)(implicit evidence$1: Applicative[F]): F[JsObject] =
        from.traverseMapFields(f.compose(_._2))
    }
  }

  implicit final lazy val jsObjectFilterIndex: FilterIndex[JsObject, String, JsValue] =
    (predicate: String => Boolean) =>
      new Traversal[JsObject, JsValue] {
        def modifyA[F[_]](f: JsValue => F[JsValue])(from: JsObject)(implicit evidence$1: Applicative[F]): F[JsObject] =
          from.traverseMapFields {
            case (key, jsv) =>
              if (predicate(key)) f(jsv) else jsv.pure[F]
          }
      }

  implicit final lazy val jsObjectIndex: Index[JsObject, String, JsValue] = Index.fromAt

}

object JsObjectOptics extends JsObjectOptics
