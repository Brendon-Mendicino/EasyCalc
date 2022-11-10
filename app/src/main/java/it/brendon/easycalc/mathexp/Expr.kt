package it.brendon.easycalc.mathexp

/**
 * [Expr] represent an expression of any sort.
 */
interface Expr {

    val latexString: String

    val needsBraces: Boolean

    val scalar: Pair<Const, Expr>?

    /**
     * Evaluate the current expression, reducing any terms present inside of it.
     * @return return the evaluated expression.
     */
    fun eval(): Expr

    fun derivative(variable: Value): Expr
}