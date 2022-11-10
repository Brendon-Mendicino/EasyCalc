package it.brendon.easycalc.mathexp

data class Const(val number: Double): Expr {

    override val needsBraces: Boolean = false

    override val latexString: String
        get() = number.toString()

    override val scalar: Pair<Const, Expr>? = null

    override fun equals(other: Any?): Boolean {
        other ?: return false

        return when (other) {
            is Const -> number == other.number
            else -> false
        }
    }

    override fun eval(): Expr = this

    override fun derivative(variable: Value): Expr = Const(0.0)

    override fun hashCode(): Int {
        return number.hashCode()
    }
}


