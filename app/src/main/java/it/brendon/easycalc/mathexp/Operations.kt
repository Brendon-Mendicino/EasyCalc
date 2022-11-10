package it.brendon.easycalc.mathexp

fun sum(e1: Expr, e2: Expr): Expr = Sum(e1, e2)

fun sub(e1: Expr, e2: Expr): Expr = Sub(e1, e2)

fun div(e1: Expr, e2: Expr): Expr = Div(e1, e2)

fun mult(e1: Expr, e2: Expr): Expr = Mult(e1, e2)

fun exp(e1: Expr, e2: Expr): Expr = Exp(e1, e2)

fun evaluateExpression(e1: Expr, e2: Expr, biFunction: (Expr, Expr) -> Expr): Expr =
    biFunction(e1, e2)

fun surroundWithBraces(e: Expr): String =
    if (e.needsBraces) "\\left( ${e.latexString} \\right)" else e.latexString