package it.brendon.easycalc.mathexp

class Mult(
    private val e1: Expr,
    private val e2: Expr
) : Operator {

    override val needsBraces: Boolean = false

    override fun eval(): Expr {
        return if (e1 is Const && e2 is Const) {
            when {
                e1.number == 0.0 -> Const(0.0)
                e2.number == 0.0 -> Const(0.0)
                else -> Const(e1.number * e2.number)
            }
        } else {
            // If the two expressions are a Const and a Mult with a scalar then,
            // create a new Mult with the two Const multiplied.
            if (e1 is Const) {
                when (e1.number) {
                    0.0 -> Const(0.0)
                    1.0 -> e2
                    else -> e2.scalar?.let {
                        Mult(Const(e1.number * it.first.number), it.second)
                    } ?: this
                }
            } else if (e2 is Const) {
                when (e2.number) {
                    0.0 -> Const(0.0)
                    1.0 -> e1
                    else -> e1.scalar?.let {
                        Mult(Const(it.first.number * e2.number), it.second)
                    } ?: this
                }
            } else {
                this
            }
        }
    }

    override val scalar: Pair<Const, Expr>? by lazy {
        when {
            e1 is Const -> Pair(e1, e2)
            e2 is Const -> Pair(e2, e1)
            else -> null
        }
    }

    override val latexString: String
        get() = "${surroundWithBraces(e1)} ${surroundWithBraces(e2)}"

    override fun derivative(variable: Value): Expr =
        Sum(
            Mult(e1.derivative(variable), e2),
            Mult(e1, e2.derivative(variable))
        )
}