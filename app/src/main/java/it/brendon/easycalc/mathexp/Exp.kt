package it.brendon.easycalc.mathexp

class Exp(
    private val base: Expr,
    private val exp: Expr
) : Operator {
    override val latexString: String
        get() = "${base.latexString}^{${exp.latexString}}"

    override val needsBraces: Boolean = false

    override val scalar: Pair<Const, Expr>? = null

    override fun eval(): Expr {
        TODO("Not yet implemented")
    }

    override fun derivative(variable: Value): Expr =
        when {
            exp is Const -> {
                base.derivative(variable)
                Exp(base, Const(exp.number - 1))
            }
            base is Const -> {
                Int
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