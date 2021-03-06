package monkey.ast

import monkey.token.Token

interface Node {
    fun tokenLiteral(): String
    fun string(): String
}

interface Statement : Node {
    fun statementNode()
}

interface Expression : Node {
    fun expressionNode()
}

class Program(val statements: MutableList<Statement> = mutableListOf()) : Node {

    override fun tokenLiteral(): String {
        return if (statements.isNotEmpty()) {
            statements[0].tokenLiteral()
        } else {
            ""
        }
    }

    override fun string(): String {
        return StringBuffer().also {
            for (s in statements) {
                it.append(s.string())
            }
        }.toString()
    }

}


class LetStatement(
    private val token: Token,
    val name: Identifier,
    val value: Expression
) : Statement {

    override fun tokenLiteral() = token.literal

    override fun statementNode() {}

    override fun string(): String {
        return StringBuffer().also {
            it.append(tokenLiteral() + " ")
            it.append(name.string())
            it.append(" = ")
            it.append(value.string())
            it.append(";")
        }.toString()
    }
}

class ReturnStatement(
    private val token: Token,
    val value: Expression
) : Statement {

    override fun tokenLiteral() = token.literal

    override fun statementNode() {}

    override fun string(): String {
        return StringBuffer().also {
            it.append(tokenLiteral() + " ")
            it.append(value.string())
            it.append(";")
        }.toString()
    }
}

class ExpressionStatement(
    private val token: Token,
    val value: Expression?
) : Statement {
    override fun tokenLiteral() = token.literal
    override fun statementNode() {}

    override fun string(): String {
        return StringBuffer().also {
            if (value != null) {
                it.append(value.string())
            } else {
                it.append("")
            }
        }.toString()
    }
}

class BlockStatement(
    private val token: Token,
    val statements: List<Statement>
) : Statement {
    override fun tokenLiteral() = token.literal
    override fun statementNode() {}

    override fun string(): String {
        return StringBuffer().also {
            for (s in statements) {
                it.append(s.string())
            }
        }.toString()
    }
}

class Identifier(private val token: Token, val value: String) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String = value
}

class IntegerLiteral(private val token: Token, val value: Long) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String = token.literal
}

class Bool(private val token: Token, val value: Boolean) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String = token.literal
}

class FunctionLiteral(
    private val token: Token,
    val parameters: List<Identifier>,
    val body: BlockStatement
) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().also {
            it.append(tokenLiteral())
            it.append("(")
            it.append(parameters.joinToString { p -> p.string() })
            it.append(")")
            it.append(body.string())
        }.toString()
    }
}

class StringLiteral(private val token: Token, val value: String) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String = token.literal
}

class ArrayLiteral(private val token: Token, val elements: List<Expression>) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().apply {
            append("[")
            append(elements.joinToString { e -> e.string() })
            append("]")
        }.toString()
    }
}

class HashLiteral(private val token: Token, val pairs: Map<Expression, Expression>) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().apply {
            append("{")
            append(pairs.map { (k, v) ->
                "${k.string()}:${v.string()}"
            }.joinToString())
            append("}")
        }.toString()
    }
}

class PrefixExpression(
    private val token: Token,
    val operator: String,
    val right: Expression?
) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().also {
            it.append("(")
            it.append(operator)
            if (right != null) {
                it.append(right.string())
            }
            it.append(")")
        }.toString()
    }
}

class InfixExpression(
    private val token: Token,
    val left: Expression?,
    val operator: String,
    val right: Expression?
) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().also {
            it.append("(")
            if (left != null) {
                it.append(left.string())
            }
            it.append(" $operator ")
            if (right != null) {
                it.append(right.string())
            }
            it.append(")")
        }.toString()
    }
}

class IfExpression(
    private val token: Token,
    val condition: Expression,
    val consequence: BlockStatement,
    val alternative: BlockStatement? = null
) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().also {
            it.append("if")
            it.append(condition.string())
            it.append(" ")
            it.append(consequence.string())
            alternative?.let { alt ->
                it.append("else ")
                it.append(alt.string())
            }
        }.toString()
    }
}

class CallExpression(
    private val token: Token,
    val function: Expression?,
    val arguments: List<Expression>
) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().also {
            function?.let { func ->
                it.append(func.string())
            }
            it.append("(")
            it.append(arguments.joinToString { exp -> exp.string() })
            it.append(")")
        }.toString()
    }
}

class IndexExpression(
    private val token: Token,
    val left: Expression?,
    val index: Expression
) : Expression {
    override fun tokenLiteral() = token.literal
    override fun expressionNode() {}

    override fun string(): String {
        return StringBuffer().apply {
            append("(")
            append(left?.string())
            append("[")
            append(index.string())
            append("])")
        }.toString()
    }
}

