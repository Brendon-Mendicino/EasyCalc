package it.brendon.easycalc.mathexp

class Log(
    private val base: Const,
    private val e: Expr
) : Expr {
    override val latexString: String
        get() = "\\log_${base.latexString}\\left( ${e.latexString} \\right)"
    override val needsBraces: Boolean
        get() = TODO("Not yet implemented")
    override val scalar: Pair<Const, Expr>?
        get() = TODO("Not yet implemented")

    override fun eval(): Expr {
        TODO("Not yet implemented")
    }

    override fun derivative(variable: Value): Expr =
        Div(
            e.derivative(variable),
            Mult(e, Log(Const(Math.E), base))
        )

}