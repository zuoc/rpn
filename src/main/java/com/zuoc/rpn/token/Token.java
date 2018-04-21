package com.zuoc.rpn.token;

/**
 * Created by zuoc on 2017/10/20.
 */
public class Token {

    private final TokenType type;

    private final String literals;

    public Token(TokenType type, String literals) {
        this.type = type;
        this.literals = literals;
    }

    public TokenType getType() {
        return type;
    }

    public String getLiterals() {
        return literals;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", literals='" + literals + '\'' +
                '}';
    }
}
