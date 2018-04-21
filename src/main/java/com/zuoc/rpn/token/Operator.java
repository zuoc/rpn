package com.zuoc.rpn.token;

import java.util.HashMap;
import java.util.Map;

/**
 * 运算符
 * Created by zuoc on 2017/10/20.
 */
public enum Operator implements TokenType {

    LEFT_PAREN("(", -1),
    RIGHT_PAREN(")", -1),

    AND("&&", 0),
    OR("||", 0),

    EQ("=", 1),
    GT(">", 1),
    LT("<", 1),
    LT_EQ("<=", 1),
    GT_EQ(">=", 1),
    NOT_EQ("!=", 1);

    private final String literals;

    private final int priority;

    private static final Map<String, Operator> operators = new HashMap(10);

    static {
        for (Operator operator : Operator.values()) {
            operators.put(operator.literals, operator);
        }
    }

    Operator(String literals, int priority) {
        this.literals = literals;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static Operator literalsOf(final String literals) {
        return operators.get(literals);
    }
}
