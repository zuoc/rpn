package com.zuoc.rpn;

import com.zuoc.rpn.token.Other;
import com.zuoc.rpn.token.Token;

import org.junit.Test;

/**
 * @author zuoc
 */
public class LexerTest {

    @Test
    public void testNextToken() {
        final Lexer lexer = new Lexer("#{a_1} > 1.3 && #{b_2} <= -0.3", "#{", "}");
        Token currentToken;
        while ((currentToken = lexer.nextToken()).getType() != Other.END) {
            if (currentToken.getType() == Other.ERROR) {
                System.out.println(currentToken);
                break;
            }
            System.out.println(currentToken);
        }
    }
}
