package com.zuoc.rpn;

import com.zuoc.rpn.exception.RpnParsingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/**
 * @author zuoc
 */
@RunWith(Parameterized.class)
public class ParserTest {

    private String expression;

    public ParserTest(String expression) {
        super();
        this.expression = expression;
    }

    @Parameterized.Parameters
    public static List<String> data() {
        String[] data = {
          "#{a_1} > 0.3 && #{b_2} <= -0.3",
          "#{a_1} > 1.3 && #{b_2} <= -0.3"
        };
        return Arrays.asList(data);
    }

    @Test
    public void testParse() throws RpnParsingException {
        final Parser parser = Parser.compile(expression, new ParserContext(new PlaceholderEval() {
            @Override
            public double eval(String placeholder) {
                if (placeholder == null) {
                    return 0.0d;
                }

                if ("a_1".equals(placeholder)) {
                    return 1.0d;
                }

                if ("b_2".equals(placeholder)) {
                    return -2.0d;
                }

                return 0.0d;
            }
        }));

        Assert.assertTrue(parser.parse());
    }

}
