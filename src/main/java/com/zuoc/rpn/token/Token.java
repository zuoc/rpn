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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token)) {
            return false;
        }

        Token token = (Token) o;

        if (!type.equals(token.type)) {
            return false;
        }
        return literals.equals(token.literals);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + literals.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", literals='" + literals + '\'' +
                '}';
    }
}
