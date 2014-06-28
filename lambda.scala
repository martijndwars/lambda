// http://docs.scala-lang.org/tutorials/tour/case-classes.html

abstract class Exp
case class Var(name: String) extends Exp
case class Fun(head: Var, body: Exp) extends Exp
case class App(f: Exp, v: Exp) extends Exp

// TODO: Apply alpha conversion, because this result is incorrect.
//val h = App(Fun(Var("x"), App(Var("x"), Var("y"))), Var("y"))
//println(reduce(f)) // App(Var("z"), Var("y"))

object Main {
  def main(args: Array[String]) {
// 0 ≡ λsz.z = λs.(λz.z)
val zero = Fun(Var("s"), Fun(Var("z"), Var("z")))

// S ≡ λwyx.y(wyx) = λw.(λy.(λx.y(wyx)))
val succ = Fun(Var("w"), Fun(Var("y"), Fun(Var("x"), App(Var("y"), App(App(Var("w"), Var("y")), Var("x"))))))

// 1 ≡ S0 = (λwyx.y(wyx))(λsz.z) = λyx.y((λsz.z)yx) = λyx.y((λz.z)x) = λyx.y(x) = λsz.s(z) = 1
println(reduce(App(succ, zero))) // Fun(Var(y),Fun(Var(x),App(Var(y),Var(x))))
  }

  // Reduce (i.e. beta-reduction) an expression
  def reduce(exp: Exp): Exp = exp match {
    case Var(name) => Var(name)
    case Fun(head, body) => Fun(head, reduce(body))
    case App(Fun(head, body), v) => reduce(sub(head, v, body))
    case App(Var(x), Var(y)) => App(Var(x), Var(y))
    case App(f, v) => reduce(App(reduce(f), reduce(v)))
    case o => o
  }

  // Substitute all occurences of a by b in exp
  def sub(a: Var, b: Exp, exp: Exp): Exp = exp match {
    case Var(x) if x == a.name => b
    case Fun(head, body) => Fun(head, sub(a, b, body))
    case App(f, v) => App(sub(a, b, f), sub(a, b, v))
    case o => o
  }

  // TODO: Test for equivalence?
}