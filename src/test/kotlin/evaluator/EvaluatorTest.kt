package evaluator

import monkey.`object`.Boolean
import monkey.`object`.Integer
import monkey.`object`.Object
import monkey.lexer.Lexer
import monkey.parser.Parser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EvaluatorTest {

    @Test
    fun evalIntegerExpression() {
        val tests = arrayOf(
                "5" to 5,
                "10" to 10,
                "-5" to -5,
                "-10" to -10,
                "5 + 5 + 5 + 5 - 10" to 10,
                "2 * 2 * 2 * 2 * 2" to 32,
                "-50 + 100 + -50" to 0,
                "5 * 2 + 10" to 20,
                "5 + 2 * 10" to 25,
                "20 + 2 * -10" to 0,
                "50 / 2 * 2 + 10" to 60,
                "2 * (5 + 10)" to 30,
                "3 * 3 * 3 + 10" to 37,
                "3 * (3 * 3) + 10" to 37,
                "(5 + 10 * 2 + 15 / 3) * 2 + -10" to 50
        )

        for ((input, expected) in tests) {
            val evaluated = testEval(input)
            testIntegerObject(evaluated, expected.toLong())
        }
    }

    @Test
    fun evalBooleanExpression() {
        val tests = arrayOf(
                "true" to true,
                "false" to false
        )

        for ((input, expected) in tests) {
            val evaluated = testEval(input)
            testBooleanObject(evaluated, expected)
        }
    }

    @Test
    fun evalBangOperator() {
        val tests = arrayOf(
                "!true" to false,
                "!false" to true,
                "!5" to false,
                "!!true" to true,
                "!!false" to false,
                "!!5" to true,
                "1 < 2" to true,
                "1 > 2" to false,
                "1 < 1" to false,
                "1 > 1" to false,
                "1 == 1" to true,
                "1 != 1" to false,
                "1 == 2" to false,
                "1 != 2" to true,
                "true == true" to true,
                "false == false" to true,
                "true == false" to false,
                "true != false" to true,
                "false != true" to true,
                "(1 < 2) == true" to true,
                "(1 < 2) == false" to false,
                "(1 > 2) == true" to false,
                "(1 > 2) == false" to true
        )

        for ((input, expected) in tests) {
            val evaluated = testEval(input)
            testBooleanObject(evaluated, expected)
        }
    }

    private fun testEval(input: String)
            = eval(Parser.newInstance(Lexer.newInstance(input)).parseProgram())

    private fun testIntegerObject(obj: Object?, expected: Long) {
        val result = obj as Integer
        assertThat(result.value).isEqualTo(expected)
    }

    private fun testBooleanObject(obj: Object?, expected: kotlin.Boolean) {
        val result = obj as Boolean
        assertThat(result.value).isEqualTo(expected)
    }

}
