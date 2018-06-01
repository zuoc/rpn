package com.zuoc.rpn;

import com.zuoc.rpn.token.Operand;
import com.zuoc.rpn.token.Operator;
import com.zuoc.rpn.token.Other;
import com.zuoc.rpn.token.Token;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author zuoc
 */
@RunWith(Parameterized.class)
public class LexerTest {

    private final Lexer lexer;
    private final Token[] expectedTokens;

    private static List<Object[]> inputParameters = new ArrayList<>();

    static {
        inputParameters.add(inputParameters1());
        inputParameters.add(inputParameters2());
    }

    private static Object[] inputParameters1() {
        return new Object[]{
                new Lexer("${1_1_0} < 1", "${", "}"),
                new Token[]{
                        new Token(Operand.PLACE_HOLDER, "1_1_0"),
                        new Token(Operator.LT, "<"),
                        new Token(Operand.NUMBER, "1")
                }
        };
    }


    private static Object[] inputParameters2() {
        return new Object[]{
                new Lexer("#{1_1_0} < 1", "${", "}"),
                new Token[]{
                        new Token(Other.ERROR, "Illegal expression '#{1_1_0} < 1', unknown char '#' in position 0"),
                }
        };
    }

    public LexerTest(Lexer lexer, Token[] expectedTokens) {
        this.lexer = lexer;
        this.expectedTokens = expectedTokens;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return inputParameters;
    }

    @Test
    public void testNextToken() {
        Token actualToken;
        for (int i = 0; i < expectedTokens.length && !(actualToken = lexer.nextToken()).equals(Lexer.END_TOKEN); i++) {
            Assert.assertEquals(expectedTokens[i], actualToken);
        }
    }
}
