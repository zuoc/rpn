package com.zuoc.rpn;

import com.zuoc.rpn.token.Operand;
import com.zuoc.rpn.token.Operator;
import com.zuoc.rpn.token.Token;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zuoc
 */

public class LexerTest {

    @Test
    public void testNextTokenXML() throws DocumentException {
        Lexer actual = createLexers().get(0);
        List<Token> tokens = createToeknsList().get(0);
        for (Token token : tokens) {
            Assert.assertEquals(token,actual.nextToken());
        }

    }

    public static Document readXML(){
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(new File(System.getProperty("user.dir") + "\\Token.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    private List<Lexer> createLexers(){
        Document document = readXML();
        List<Element> lexers = document.getRootElement().elements("lexer");
        List<Lexer> result = new ArrayList<>();
        for (Element lexer : lexers) {
            String input = lexer.element("input").getTextTrim();
            String prefix = lexer.element("prefix").getTextTrim();
            String suffix = lexer.element("suffix").getTextTrim();
            result.add(new Lexer(input, prefix, suffix));
        }
        return result;
    }

    private List<List<Token>> createToeknsList(){
        Document document = readXML();
        List<Element> tokensList = document.getRootElement().elements("tokens");
        ArrayList<List<Token>> result = new ArrayList<>();
        for (Element tokens : tokensList) {
            List<Token> toekns = createToekns(tokens);
            result.add(toekns);
        }
        return result;
    }

    private List<Token> createToekns(Element tokens){
        List<Element> token = tokens.elements("token");
        ArrayList<Token> result = new ArrayList<>();
        for (int i = 0; i < token.size()-1; i++) {
            Element element = (Element) token.get(i).elements().get(0);
            String tokenType = element.getQualifiedName();
            String literals = element.getTextTrim();
            if ("PLACE_HOLDER".equals(tokenType)){
                result.add(new Token(Operand.valueOf(tokenType),literals));
            }
            if ("NUMBER".equals(tokenType)){
                result.add(new Token(Operand.valueOf(tokenType),literals));
            }
            if ("BOOLEAN".equals(tokenType)){
                result.add(new Token(Operand.valueOf(tokenType),literals));
            }
            if ("LEFT_PAREN".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("RIGHT_PAREN".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("AND".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("OR".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("EQ".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("GT".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("LT".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("LT_EQ".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("GT_EQ".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
            if ("NOT_EQ".equals(tokenType)){
                result.add(new Token(Operator.valueOf(tokenType),literals));
            }
        }
        return result;
    }
}
