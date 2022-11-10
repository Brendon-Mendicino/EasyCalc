package it.brendon.easycalc.mathexp

internal class ExprImpl(
    override val latexString: String
): Expr {
    override val needsBraces: Boolean
        get() = TODO("Not yet implemented")
    override val scalar: Pair<Const, Expr>?
        get() = TODO("Not yet implemented")

    override fun eval(): Expr {
        TODO("Not yet implemented")
    }

    override fun derivative(variable: Value): Expr {
        TODO("Not yet implemented")
    }
}