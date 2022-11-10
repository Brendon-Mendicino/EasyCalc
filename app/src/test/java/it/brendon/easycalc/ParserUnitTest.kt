package it.brendon.easycalc

import it.brendon.easycalc.mathexp.*
import it.brendon.easycalc.parser.Parser
import org.junit.Assert.*
import org.junit.Test

class ParserUnitTest {
    @Test
    fun parenthesisNotMatched_isCorrect() {
        val expression = "1 + x ( 2 - 10 ) / ( 9 ^ y"
        val parser = Parser(expression, setOf("x", "y"))

        println(parser.checkFormat())

        assertEquals("Number of errors", 1, parser.checkFormat().size)
        assertEquals("Error position", expression.indexOfLast { it == '(' }, parser.checkFormat()[0].first)
    }

    @Test
    fun testValidityOfExpression() {
        val expr = Mult(
            Sum(
                Exp(
                    Value("x"),
                    Const(2.5)
                ),
                Sub(
                    Value("y"),
                    Const(5.0)
                )
            ),
            Value("x")
        )
        val string = "(x^2.5 + y - 5.0)x"

        val parser = Parser(string, setOf("x", "y"))

        assertEquals(true, parser.rawExpression()?.equals(expr))
    }
}