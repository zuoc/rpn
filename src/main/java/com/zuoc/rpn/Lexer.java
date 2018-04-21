package com.zuoc.rpn;


import com.zuoc.rpn.token.Operand;
import com.zuoc.rpn.token.Operator;
import com.zuoc.rpn.token.Other;
import com.zuoc.rpn.token.Token;

/**
 * 词法解析器
 * Created by zuoc on 2017/10/20.
 */
public final class Lexer {

    /**
     * End of input.
     */
    private static final byte EOI = 0x1A;

    private final String input;

    private final String placeholderPrefix;

    private final String placeholderSuffix;

    private int offset;

    protected Lexer(String input, String placeholderPrefix, String placeholderSuffix) {
        this.input = input;
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
    }

    protected Token nextToken() {
        skipWhitespace();
        if (isPlaceholderPrefix()) {
            return scanPlaceholder();
        } else if (isNumberOperandBegin()) {
            return scanNumberOperand();
        } else if (isOperatorBegin()) {
            return scanOperator();
        } else if (isEnd()) {
            return new Token(Other.END, "");
        } else {
            return new Token(Other.ERROR, "");
        }
    }

    private boolean isPlaceholderPrefix() {
        for (int i = 0; i < placeholderPrefix.length(); i++) {
            if (placeholderPrefix.charAt(i) != charAt(offset + i)) {
                return false;
            }
        }
        return true;
    }

    private Token scanPlaceholder() {
        offset += placeholderPrefix.length();

        final int nextSuffix = input.indexOf(placeholderSuffix, offset);
        if (nextSuffix == -1) {
            // miss suffix
            return new Token(Other.ERROR, "miss suffix");
        }

        int length = 0;
        while (offset + length < nextSuffix && isPlaceholderChar(charAt(offset + length))) {
            length++;
        }

        if (offset + length == nextSuffix) {
            final String literals = input.substring(offset, offset + length);
            offset += (length + placeholderSuffix.length());
            return new Token(Operand.PLACE_HOLDER, literals);
        }

        final int nextPrefix = input.indexOf(placeholderPrefix, offset + length);
        if (offset + length == nextPrefix) {
            // nesting placeholder
            return new Token(Other.ERROR, "nesting placeholder");
        }

        return new Token(Other.ERROR, "miss suffix");
    }

    private boolean isPlaceholderChar(char ch) {
        return isAlphabet(ch) || isDigital(ch) || '_' == ch;
    }

    private boolean isNumberOperandBegin() {
        return isDigital(charAt(offset)) || ('-' == charAt(offset) && isDigital(charAt(offset + 1)));
    }

    private Token scanNumberOperand() {
        int length = 0;
        if ('-' == charAt(offset + length)) {
            length++;
        }
        length += getDigitalLength(offset + length);
        if ('.' == charAt(offset + length)) {
            length++;
            if (length == (length += getDigitalLength(offset + length))) {
                return new Token(Other.ERROR, "");
            }
        }
        final String literals = input.substring(offset, offset + length);
        offset += length;
        return new Token(Operand.NUMBER, literals);
    }

    private int getDigitalLength(final int offset) {
        int length = 0;
        while (isDigital(charAt(offset + length))) {
            length++;
        }
        return length;
    }

    private boolean isOperatorBegin() {
        return isOperator(charAt(offset));
    }

    private Token scanOperator() {
        int length = 0;
        while (isOperator(charAt(offset + length))) {
            length++;
        }
        String literals = input.substring(offset, offset + length);
        Operator operator;
        while ((operator = Operator.literalsOf(literals)) == null) {
            if (--length < 0) {
                return new Token(Other.ERROR, "");
            }
            literals = input.substring(offset, offset + length);
        }
        offset += length;
        return new Token(operator, literals);
    }

    private int skipWhitespace() {
        int length = 0;
        while (isWhitespace(charAt(offset + length))) {
            length++;
        }
        return offset += length;
    }

    private boolean isEnd() {
        return charAt(offset) == EOI;
    }

    private boolean isWhitespace(final char ch) {
        return ch <= 32 && EOI != ch || 160 == ch || ch >= 0x7F && ch <= 0xA0;
    }

    private boolean isAlphabet(final char ch) {
        return ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z';
    }

    private boolean isDigital(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isOperator(final char ch) {
        return '(' == ch || ')' == ch ||
                '=' == ch || '!' == ch ||
                '>' == ch || '<' == ch ||
                '&' == ch || '|' == ch;
    }

    private char charAt(final int index) {
        return index >= input.length() ? (char) EOI : input.charAt(index);
    }
}
