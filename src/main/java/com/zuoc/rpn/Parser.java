package com.zuoc.rpn;

import com.zuoc.rpn.exception.RpnParsingException;
import com.zuoc.rpn.token.Operand;
import com.zuoc.rpn.token.Operator;
import com.zuoc.rpn.token.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.zuoc.rpn.token.Operand.*;
import static com.zuoc.rpn.token.Operator.*;
import static com.zuoc.rpn.token.Other.*;

/**
 * Created by zuoc on 2017/10/20.
 */
public final class Parser {

    private final String expression;

    private final PlaceholderEval eval;

    private final Lexer lexer;

    private final List<Token> postfix = new ArrayList<>();

    public static final ConcurrentMap<String, Parser> PARSER_CACHE = new ConcurrentHashMap<>();

    public static Parser compile(final String expression, final ParserContext context) throws RpnParsingException {
        Parser current = PARSER_CACHE.get(expression);
        if (current == null) {
            current = new Parser(expression, context);
            final Parser previous = PARSER_CACHE.putIfAbsent(expression, current);
            if (previous != null) {
                current = previous;
            }
        }
        return current;
    }

    private Parser(final String expression, final ParserContext context) throws RpnParsingException {
        this.expression = expression;
        this.eval = context.eval;
        this.lexer = new Lexer(expression, context.prefix, context.suffix);
        transform();
    }

    public boolean parse() throws RpnParsingException {
        // see https://zh.wikipedia.org/wiki/%E9%80%86%E6%B3%A2%E5%85%B0%E8%A1%A8%E7%A4%BA%E6%B3%95
        final Deque<Token> stack = new LinkedList<>();
        Token token;
        for (int cursor = 0; cursor < postfix.size() && (token = postfix.get(cursor)) != null; cursor++) {
            if (token.getType() instanceof Operand) {
                stack.push(token);
            } else if (token.getType() instanceof Operator) {
                final Operator operator = (Operator) token.getType();
                Token result;
                switch (operator) {
                    case AND:
                    case OR:
                        result = calculateLogical(operator, stack.pop(), stack.pop());
                        break;
                    case EQ:
                    case GT:
                    case LT:
                    case LT_EQ:
                    case GT_EQ:
                    case NOT_EQ:
                        result = calculateRelational(operator, stack.pop(), stack.pop());
                        break;
                    default:
                        throw new RpnParsingException("Unknown error evaluating expression '" + expression + "'");
                }
                stack.push(result);
            }
        }

        if (stack.size() != 1 || stack.peek().getType() != BOOLEAN) {
            throw new RpnParsingException("Unknown error evaluating expression '" + expression + "'");
        }

        return Boolean.valueOf(stack.pop().getLiterals());
    }

    /**
     * 计算关系运算符
     *
     * @param operator 关系运算符
     * @return BOOLEAN 类型的 Token
     */
    private Token calculateRelational(final Operator operator, final Token tokenA, final Token tokenB) throws RpnParsingException {
        final double operandA = getDoubleValue(tokenA);
        final double operandB = getDoubleValue(tokenB);

        boolean result;
        switch (operator) {
            case EQ:
                result = operandB == operandA;
                break;
            case GT:
                result = operandB > operandA;
                break;
            case LT:
                result = operandB < operandA;
                break;
            case LT_EQ:
                result = operandB <= operandA;
                break;
            case GT_EQ:
                result = operandB >= operandA;
                break;
            case NOT_EQ:
                result = operandB != operandA;
                break;
            default:
                throw new RpnParsingException("Unknown error evaluating expression '" + expression + "'");
        }

        return new Token(BOOLEAN, String.valueOf(result));
    }

    /**
     * 计算逻辑运算符
     *
     * @param operator 逻辑运算符
     * @return BOOLEAN 类型的 Token
     */
    private Token calculateLogical(final Operator operator, final Token tokenA, final Token tokenB) throws RpnParsingException {
        final boolean operandA = getBooleanValue(tokenA);
        final boolean operandB = getBooleanValue(tokenB);

        boolean result;
        switch (operator) {
            case AND:
                result = operandA && operandB;
                break;
            case OR:
                result = operandA || operandB;
                break;
            default:
                throw new RpnParsingException("Unknown error evaluating expression '" + expression + "'");
        }

        return new Token(BOOLEAN, String.valueOf(result));
    }

    private double getDoubleValue(Token token) {
        if (token.getType() == PLACE_HOLDER) {
            return eval.eval(token.getLiterals());
        }

        if (token.getType() == NUMBER) {
            try {
                return Double.valueOf(token.getLiterals());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("");
            }
        }

        throw new IllegalArgumentException("");
    }

    private boolean getBooleanValue(Token token) {
        if (token.getType() == BOOLEAN) {
            if ("true".equalsIgnoreCase(token.getLiterals())) {
                return true;
            }

            if ("false".equalsIgnoreCase(token.getLiterals())) {
                return false;
            }
        }

        throw new IllegalArgumentException("");
    }

    /**
     * see https://zh.wikipedia.org/wiki/%E8%B0%83%E5%BA%A6%E5%9C%BA%E7%AE%97%E6%B3%95
     */
    private void transform() throws RpnParsingException {
        final Deque<Token> stack = new LinkedList<>();
        Token currentToken;
        int paren = 0;
        while (END != ((currentToken = lexer.nextToken()).getType())) {
            if (ERROR == currentToken.getType()) {
                throw new RpnParsingException(currentToken.getLiterals());
            }
            if (currentToken.getType() instanceof Operand) {
                postfix.add(currentToken);

            } else if (currentToken.getType() instanceof Operator) {
                final Operator currentOperator = (Operator) currentToken.getType();

                switch (currentOperator) {
                    case LEFT_PAREN:
                        stack.push(currentToken);
                        paren++;
                        break;
                    case RIGHT_PAREN:
                        if (paren <= 0) {
                            throw new RpnParsingException("Unknown error evaluating expression '" + expression + "'");
                        }
                        while (LEFT_PAREN != stack.peek().getType()) {
                            postfix.add(stack.pop());
                        }
                        stack.pop();
                        paren--;
                        break;
                    default:
                        // Operator 均为左结合性操作符
                        while (stack.peek() != null && ((Operator) stack.peek().getType()).getPriority() >= currentOperator.getPriority()) {
                            postfix.add(stack.pop());
                        }
                        stack.push(currentToken);
                        break;
                }
            }
        }

        if (paren != 0) {
            throw new RpnParsingException("Unknown error evaluating expression '" + expression + "'");
        }

        while (stack.peek() != null) {
            postfix.add(stack.pop());
        }
    }

}
