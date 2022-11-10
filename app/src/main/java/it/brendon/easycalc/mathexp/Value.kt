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

    override fun equals(other: Any?): Boolean {
        other ?: return false

        return when (other) {
            is Value -> symbol == other.symbol
            else -> false
        }
    }

    override fun derivative(variable: Value): Expr =
        if (variable.symbol == symbol) Const(1.0) else Const(0.0)

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}