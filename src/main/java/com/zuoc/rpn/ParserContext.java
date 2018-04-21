package com.zuoc.rpn;

/**
 * @author zuoc
 */
public interface ParserContext {

    default String getPlaceholderPrefix() {
        return "#{";
    }

    default String getPlaceholderSuffix() {
        return "}";
    }

    Double getPlaceholderValue(final String placeholder);

}
