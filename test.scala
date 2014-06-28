// --- General

val a = Var("x")
println(reduce(a)) // Var("x")

val b = Fun(Var("x"), Var("y"))
println(reduce(b)) // Fun(Var("x"), Var("y"))

val c = App(Fun(Var("x"), Var("y")), Var("z"))
println(reduce(c)) // Var("y")

val d = App(Fun(Var("x"), Var("x")), Var("z"))
println(reduce(d)) // Var("z")

val e = App(Fun(Var("x"), App(Var("x"), Var("y"))), Var("z"))
println(reduce(e)) // App(Var("z"), Var("y"))

val f = App(Fun(Var("x"), Var("y")), Var("z"))
println(reduce(f)) // Var("y")

// (λx.xy)(λs.z) = (λs.z)y = z
val g = App(Fun(Var("x"), App(Var("x"), Var("y"))), Fun(Var("s"), Var("z")))
println(reduce(g)) // Var("z")

// --- Arithmetic

// 0 ≡ λsz.z = λs.(λz.z)
val zero = Fun(Var("s"), Fun(Var("z"), Var("z")))

// S ≡ λwyx.y(wyx) = λw.(λy.(λx.y(wyx)))
val succ = Fun(Var("w"), Fun(Var("y"), Fun(Var("x"), App(Var("y"), App(App(Var("w"), Var("y")), Var("x"))))))

// 1 ≡ S0 = (λwyx.y(wyx))(λsz.z) = λyx.y((λsz.z)yx) = λyx.y((λz.z)x) = λyx.y(x) = λsz.s(z) = 1
println(reduce(App(succ, zero))) // Fun(Var(y),Fun(Var(x),App(Var(y),Var(x))))