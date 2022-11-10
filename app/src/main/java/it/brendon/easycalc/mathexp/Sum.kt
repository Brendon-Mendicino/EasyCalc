package it.brendon.easycalc.mathexp

class Sum(
    private val e1: Expr,
    private val e2: Expr
) : Operator {

    override val needsBraces: Boolean = true

    override val scalar: Pair<Const, Expr>? = null

    override fun eval(): Expr {
        // Evaluate expressions before operating
        val e1 = e1.eval()
        val e2 = e2.eval()

        return if (e1 is Const && e2 is Const) {
            when {
                e1.number == 0.0 -> Const(e2.number)
                e2.number == 0.0 -> Const(e2.number)
                else -> Const(e1.number + e2.number)
            }
        } else if (e1 is Sum && e2 is Const) {
            e1.getPair()?.let {
                Sum(
                    Const(it.first.number + e2.number),
                    it.second
                )
            } ?: Sum(e1, e2)
        } else if (e2 is Sum && e1 is Const) {
            e2.getPair()?.let {
                Sum(
                    Const(it.first.number + e1.number),
                    it.second
                )
            } ?: Sum(e1, e2)
        } else {
            // Take the const in the front
            when (e2) {
                is Const -> Sum(e2, e1)
                else -> Sum(e1, e2)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        return when (other) {
            is Sum -> (e1 == other.e1 && e2 == other.e2) || (e1 == other.e2 && e2 == other.e1)
            else -> false
        }
    }

    internal fun getPair(): Pair<Const, Expr>? {
        return when {
            e1 is Const -> Pair(e1, e2)
            e2 is Const -> Pair(e2, e1)
            else -> null
        }
    }

    override val latexString: String
        get() = "${e1.latexString} + ${e2.latexString}"

    override fun derivative(variable: Value): Expr =
        Sum(e1.derivative(variable), e2.derivative(variable))

}