package it.brendon.easycalc.mathexp

class Exp(
    private val base: Expr,
    private val exp: Expr
) : Operator {
    override val latexString: String
        get() = "${surroundWithBraces(base)}^{${exp.latexString}}"

    override val needsBraces: Boolean = false

    override val scalar: Pair<Const, Expr>? = null

    override fun eval(): Expr {
        // Evaluate expressions before operating
        val exp = exp.eval()
        val base = base.eval()

        return when {
            exp is Const && exp.number == 1.0 -> base
            exp is Const && exp.number == 0.0 -> Const(1.0)
            else -> Exp(base, exp)
        }
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        return when (other) {
            is Exp -> base == other.base && exp == other.exp
            else -> false
        }
    }

    override fun derivative(variable: Value): Expr {
        return when {
            exp is Const -> {
                Mult(
                    Const(exp.number),
                    Mult(
                        base.derivative(variable),
                        Exp(base, Const(exp.number - 1))
                    )
                )
            }
            base is Const -> {
                if (base.number == Math.E)
                    Mult(
                        exp.derivative(variable),
                        this
                    )
                else
                    Mult(
                        exp.derivative(variable),
                        Mult(
                            Log(Const(Math.E), base),
                            this
                        )
                    )
            }
            else -> {
                Mult(
                    Mult(base, Log(Const(Math.E), exp)).derivative(variable),
                    this
                )
            }
        }
    }

}