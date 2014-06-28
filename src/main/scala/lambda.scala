abstract class Exp
case class Var(name: String) extends Exp
case class Fun(head: Var, body: Exp) extends Exp
case class App(f: Exp, v: Exp) extends Exp

object Lambda {
  def main(args: Array[String]) {
  }

  // Reduce (i.e. beta-reduction) an expression
  def reduce(exp: Exp): Exp = exp match {
    case Var(name) => Var(name)
    case Fun(head, body) => Fun(head, reduce(body))
    case App(Fun(head, body), v) => reduce(sub(head, v, body))
    case App(App(x, y), z) => reduce(App(reduce(App(x, y)), reduce(z)))
    case App(f, v) => App(reduce(f), reduce(v))
    case o => o
  }

  // Substitute all occurences of a by b in exp
  def sub(a: Var, b: Exp, exp: Exp): Exp = exp match {
    case Var(x) if x == a.name => b
    case Fun(head, body) => Fun(head, sub(a, b, body))
    case App(f, v) => App(sub(a, b, f), sub(a, b, v))
    case o => o
  }
}