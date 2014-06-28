import org.scalatest.FlatSpec
import Lambda._

class LambdaSpec extends FlatSpec {
  "A lambda expression" should "reduce 'x' to 'x'" in {
    val e = Var("x")
    assert(reduce(e) === Var("x"))
  }

  "A lambda expression" should "reduce 'λx.y' to 'λx.y'" in {
    val e = Fun(Var("x"), Var("y"))
    assert(reduce(e) === Fun(Var("x"), Var("y")))
  }

  "A lambda expression" should "reduce 'λx.y(z)' to 'y'" in {
    val e = App(Fun(Var("x"), Var("y")), Var("z"))
    assert(reduce(e) === Var("y"))
  }

  "A lambda expression" should "reduce 'λx.x(z)' to 'z'" in {
    val e = App(Fun(Var("x"), Var("x")), Var("z"))
    assert(reduce(e) === Var("z"))
  }

  "A lambda expression" should "reduce 'λx.xy(z)' to 'zy'" in {
    val e = App(Fun(Var("x"), App(Var("x"), Var("y"))), Var("z"))
    assert(reduce(e) === App(Var("z"), Var("y")))
  }

  "A lambda expression" should "reduce body after first reduction, i.e. (λx.xy)z to zy" in {
    val e = App(Fun(Var("x"), App(Var("x"), Var("y"))), Var("z"))
    assert(reduce(e) === App(Var("z"), Var("y")))
  }

  "A lambda expression" should "reduce body after first reduction, i.e. (λx.xy)(λs.z) to z" in {
    val e = App(Fun(Var("x"), App(Var("x"), Var("y"))), Fun(Var("s"), Var("z")))
    assert(reduce(e) === Var("z"))
  }

  "A lambda expression" should "reduce 'x(λy.z)' to 'x(λy.z)'" in {
    val e = App(Var("x"), Fun(Var("y"), Var("z")))
    assert(reduce(e) === App(Var("x"), Fun(Var("y"), Var("z"))))
  }

  "A lambda expression" should "reduce S0 to 1'" in {
    // 0 ≡ λsz.z = λs.(λz.z)
    val zero = Fun(Var("s"), Fun(Var("z"), Var("z")))

    // S ≡ λwyx.y(wyx) = λw.(λy.(λx.y(wyx)))
    val succ = Fun(Var("w"), Fun(Var("y"), Fun(Var("x"), App(Var("y"), App(App(Var("w"), Var("y")), Var("x"))))))

    // 1 ≡ S0 = (λwyx.y(wyx))(λsz.z) = λyx.y((λsz.z)yx) = λyx.y((λz.z)x) = λyx.y(x) = λsz.s(z) = 1
    assert(reduce(App(succ, zero)) == Fun(Var("y"),Fun(Var("x"),App(Var("y"),Var("x")))))
  }
}