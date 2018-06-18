package com.zuoc.rpn;

import com.zuoc.rpn.exception.RpnParsingException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zuoc
 */

public class ParserTest {

    @Test
    public void testParse() throws RpnParsingException {
        final Parser parser = Parser.compile(createExpression().get(0), new ParserContext(new PlaceholderEval() {
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

    private List<String> createExpression(){
        Document document = LexerTest.readXML();
        List<Element> lexers = document.getRootElement().elements("lexer");
        ArrayList<String > result = new ArrayList<String>();
        for (Element lexer : lexers) {
            String input = lexer.element("input").getTextTrim();
            result.add(input);
        }
        return result;
    }

}
