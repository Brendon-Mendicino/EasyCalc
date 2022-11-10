package it.brendon.easycalc

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
}