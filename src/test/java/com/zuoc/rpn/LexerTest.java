package com.zuoc.rpn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author zuoc
 */
@RunWith(Parameterized.class)
public class LexerTest {

    private Object[] literals;
    private Lexer lexer;

    public LexerTest(Lexer lexer,Object[] literals) {
        super();
        this.literals = literals;
        this.lexer = lexer;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
          { new Lexer("#{a_1} > 1.3 && #{b_2} <= -0.3", "#{", "}"), new Object[]{"a_1",">","1.3","&&","b_2","<=","-0.3"} },
          { new Lexer("#[a_1] > 1.3 && #[b_2] <= -0.3", "#[", "]"), new Object[]{"a_1",">","1.3","&&","b_2","<=","-0.3"} }
        };
        return Arrays.asList(data);
    }

    @Test
    public void testNextToken() {
        for (Object expected : literals) {
            String actual = lexer.nextToken().getLiterals();
            Assert.assertEquals(expected,actual);
        }
    }
}
