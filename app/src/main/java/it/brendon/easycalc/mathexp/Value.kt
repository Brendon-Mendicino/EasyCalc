package it.brendon.easycalc.mathexp

class Value(
    private val symbol: String
) : Expr {

    override val needsBraces: Boolean
        get() = false

    override val latexString: String
        get() = symbol

    override val scalar: Pair<Const, Expr>? = null

    override fun eval(): Expr = this

    override fun derivative(variable: Value): Expr =
        if (variable.symbol == symbol) Const(1.0) else Const(0.0)
}