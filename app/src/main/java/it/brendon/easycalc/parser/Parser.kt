package it.brendon.easycalc.parser

import it.brendon.easycalc.mathexp.*
import java.util.*
import kotlin.properties.Delegates


internal enum class Operation {
    Summation,
    Subtraction,
    Multiplication,
    Division,
    Exponentiation
}

internal enum class Prev {
    OpenBrace,
    ClosedBrace,
    Blank,
    Const,
    Variable,
    Operator,
    Point,
}

internal class CharIteratorWithCurrent(
    val iterator: CharIterator
) : CharIterator() {

    var current by Delegates.notNull<Char>()

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun nextChar(): Char {
        current = iterator.nextChar()
        return current
    }

    fun nextNumber(): Double? {
        if (current !in '0'..'9') {
            while (hasNext() && nextChar() !in '0'..'9') {
                if (!hasNext()) return null
            }
        }

        val string = StringBuilder()
        do {
            string.append(current)

            if (nextChar() in '0'..'9') {
            }
        } while (hasNext())

        return string.toString().toDoubleOrNull()
    }

    fun nextOperation(): Operation? {
        while (hasNext()) {
            return when (nextChar()) {
                '+' -> Operation.Summation
                '-' -> Operation.Subtraction
                '*' -> Operation.Multiplication
                '/' -> Operation.Division
                '^' -> Operation.Exponentiation
                ' ' -> continue
                else -> Operation.Multiplication
            }
        }
        return null
    }
}

/**
 * This class parses the [string] value, translating it into a valid
 * expression [Expr]
 */
class Parser(
    private val string: String,
    private val variables: Set<String>
) {

    companion object {
        private const val UNMATCHED_BRACE = "Unmatched brace"
        private const val CHARACTER_NOT_ALLOWED = "Character is not allowed"
        private const val OPERATOR_AFTER_BRACE = "Operator after brace not allowed"
        private const val CONSEQUENT_OPERATOR = "Cannot have subsequent operators"
        private const val VARIABLE_NOT_FOUND = "Variable not found"
        private const val NUMBER_WRONG_FORMAT = "Number has wrong format"
        private const val FLOATING_POINT_WRONG_FORMAT = "Floating point wrong format: "
        private const val NUMBER_MUST_GO_AFTER_POINT = "Number must be placed after a point"
        private const val CURLY_BRACE_MUST_FOLLOW_UNDERSCORE = "Curly brace must follow underscore"
        private const val UNMATCHED_CURLY = "Unmatched curly brace"
        private const val CANNOT_NEST_CURLY = "Cannot nest curly braces"
        private const val ONLY_ALPHA_ALLOWED_INSIDE_CURLY =
            "Only alphanumeric characters allowed inside curly braces"

        /**
         *
         * @param string [String] to operate any the check on
         * @param variables set of variables
         * @return return a list of errors, each [Pair] contains the index of the error and the error message
         */
        fun checkFormat(string: String, variables: Set<String>): List<Pair<Int, String>> {
            var prev: Prev = Prev.OpenBrace
            val variable = StringBuilder()
            val braceStack = ArrayDeque<Int>()
            val operatorStack = ArrayDeque<Pair<Int, Char>>()
            val errorLog = ArrayList<Pair<Int, String>>()
            var curlyBraceOpened = false

            for (i in string.indices) {
                when (string[i]) {
                    ' ' -> prev = Prev.Blank
                    '(' -> {
                        braceStack.push(i)
                        prev = Prev.OpenBrace
                    }
                    ')' -> {
                        if (braceStack.isEmpty()) errorLog.add(
                            Pair(i, UNMATCHED_BRACE)
                        ) else braceStack.pop()

                        prev = Prev.ClosedBrace
                    }
                    '+', '-' -> {
                        if (operatorStack.isNotEmpty()) {
                            errorLog.add(Pair(i, CONSEQUENT_OPERATOR))
                            operatorStack.pop()
                        }
                        when (prev) {
                            Prev.Operator -> errorLog.add(Pair(i, CONSEQUENT_OPERATOR))
                            else -> operatorStack.add(Pair(i, string[i]))
                        }
                        prev = Prev.Operator
                    }
                    '*', '/', '^' -> {
                        if (operatorStack.isNotEmpty()) {
                            errorLog.add(Pair(i, CONSEQUENT_OPERATOR))
                            operatorStack.pop()
                        }
                        when (prev) {
                            Prev.Operator -> errorLog.add(Pair(i, CONSEQUENT_OPERATOR))
                            Prev.OpenBrace -> errorLog.add(Pair(i, OPERATOR_AFTER_BRACE))
                            else -> operatorStack.add(Pair(i, string[i]))
                        }
                        prev = Prev.Operator
                    }
                    in 'A'..'Z', in 'a'..'z', '_', '{', '}' -> {
                        variable.append(string[i])
                        if (variables.contains(variable.toString()))
                            variable.clear()

                        // The checks on the sub-part of the variable cannot be splitted
                        // into another case
                        if (string[i] == '_' && (i + 1 >= string.length || string[i + 1] != '{')) {
                            errorLog.add(Pair(i, CURLY_BRACE_MUST_FOLLOW_UNDERSCORE))
                        }
                        if (string[i] == '{') {
                            if (curlyBraceOpened) {
                                errorLog.add(Pair(i, CANNOT_NEST_CURLY))
                            }
                            if (i - 1 < 0 || string[i - 1] != '_') {
                                errorLog.add(Pair(i, CURLY_BRACE_MUST_FOLLOW_UNDERSCORE))
                            }
                            curlyBraceOpened = true
                        }
                        if (string[i] == '}' && !curlyBraceOpened) {
                            errorLog.add(Pair(i, UNMATCHED_CURLY))
                        }

                        // Exponent check ( 1010e-10 )
                        if (i - 1 >= 0 && i + 2 < string.length && string[i] == 'e') {
                            if (string[i - 1] in '0'..'9' &&
                                (string[i + 1] == '-' || string[i + 1] == '+') &&
                                string[i + 2] in '0'..'9'
                            ) {
                                variable.clear()
                            }
                        }

                        prev = Prev.Variable
                    }
                    in '0'..'9' -> prev = Prev.Const
                    '.' -> {
                        if (prev != Prev.Const) {
                            errorLog.add(Pair(i, FLOATING_POINT_WRONG_FORMAT))
                        }
                        if (i + 1 >= string.length) {
                            errorLog.add(Pair(i, NUMBER_WRONG_FORMAT))
                        } else if (string[i + 1] !in '0'..'9') {
                            errorLog.add(Pair(i + 1, NUMBER_MUST_GO_AFTER_POINT))
                        }
                        prev = Prev.Point
                    }
                    else -> errorLog.add(Pair(i, CHARACTER_NOT_ALLOWED))
                }

                if (curlyBraceOpened && prev != Prev.Variable) {
                    errorLog.add(Pair(i, ONLY_ALPHA_ALLOWED_INSIDE_CURLY))
                }

                if (prev != Prev.Variable && variable.isNotEmpty()) {
                    errorLog.add(Pair(i - variable.length, "\"${variable}\": $VARIABLE_NOT_FOUND"))
                    variable.clear()
                }

                if (prev != Prev.Operator && prev != Prev.Blank && operatorStack.isNotEmpty()) {
                    operatorStack.pop()
                }
            }

            braceStack.forEach { errorLog.add(Pair(it, UNMATCHED_BRACE)) }

            if (variable.isNotEmpty()) {
                errorLog.add(
                    Pair(
                        string.length - variable.length,
                        "\"$variable\": $VARIABLE_NOT_FOUND"
                    )
                )
            }

            return errorLog
        }
    }

    fun checkFormat(): List<Pair<Int, String>> = Companion.checkFormat(string, variables)

    fun rawExpression(): Expr? {
        val iterator = CharIteratorWithCurrent(string.iterator())

        return rawExpression(iterator)
    }

    private fun rawExpression(iterator: CharIteratorWithCurrent): Expr? {
        // TODO: si potrebbero usare degli indici di stop per gli stack, invece delle chiamate ricorsive,
        // esempio: al posto della chiamata ricorsiva, se nello stack sono presetni due elementi,
        // posso avare var index = 2, e posso  fare pop() allo stack  solo fino alla seconda posizione.
        val exprStack = Stack<Expr>()
        val operatorStack = Stack<Operation>()

        val number = StringBuilder()
        val variable = StringBuilder()

        while (iterator.hasNext()) {
            if (iterator.nextChar() == ' ')
                continue

            if (iterator.current in '0'..'9') {
                number.append(iterator.current)
            } else if (iterator.current.isLetter()) {

            }


            if (iterator.current == '(') {
                if (!iterator.hasNext())
                    return null
                rawExpression(iterator)?.let { exprStack.push(it) } ?: return null
            }
        }

        return if (exprStack.size == 1) exprStack.pop() else null
    }
}
//brenty :)
//flavy ?:>