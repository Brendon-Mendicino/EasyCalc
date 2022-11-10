package it.brendon.easycalc.mathexp

class Div(
    private val e1: Expr,
    private val e2: Expr
) : Operator {

    override val latexString: String
        get() = "\\frac{ ${e1.latexString} }{ ${e2.latexString} }"

    override val needsBraces: Boolean = true

    override val scalar: Pair<Const, Expr>? = null

    override fun eval(): Expr {
        return if (e1 is Const && e2 is Const) {
            when {
                e1.number == 0.0 -> Const(0.0)
                e2.number == 1.0 -> e1
                else -> Const(e1.number / e2.number)
            }
        } else {
            this
        }
    }

    override fun derivative(variable: Value): Expr =
        Div(
            Sub(
                Mult(e1.derivative(variable), e2),
                Mult(e1, e2.derivative(variable))
            ),
            Exp(e2, Const(2.0))
        )
}