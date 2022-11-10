package it.brendon.easycalc.mathexp

class Sub(
    private val e1: Expr,
    private val e2: Expr
) : Operator {

    override val needsBraces: Boolean = true

    override val scalar: Pair<Const, Expr>? = null

    override fun eval(): Expr {
        // Eval expressions before operating
        val e1 = e1.eval()
        val e2 = e2.eval()

        return if (e1 is Const && e2 is Const) {
            when {
                e1.number == 0.0 -> Const(e2.number)
                e2.number == 0.0 -> Const(e2.number)
                else -> Const(e1.number - e2.number)
            }
        } else {
            Sub(e1, e2)
        }
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        return when (other) {
            is Sub -> (e1 == other.e1 && e2 == other.e2)
            else -> false
        }
    }

    override val latexString: String
        get() = "${e1.latexString} - ${e2.latexString}"

    override fun derivative(variable: Value): Expr =
        Sub(e1.derivative(variable), e2.derivative(variable))
}