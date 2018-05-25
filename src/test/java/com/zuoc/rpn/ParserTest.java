package com.zuoc.rpn;

import com.zuoc.rpn.exception.RpnParsingException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zuoc
 */
public class ParserTest {

    @Test
    public void testParse() throws RpnParsingException {
        final Parser parser = Parser.compile("#{a_1} > 0.3 && #{b_2} <= -0.3", new ParserContext(new PlaceholderEval() {
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
