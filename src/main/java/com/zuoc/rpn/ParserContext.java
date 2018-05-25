package com.zuoc.rpn;

/**
 * @author zuoc
 */
public final class ParserContext {

    private static final String DEFAULT_PREFIX = "#{";

    private static final String DEFAULT_SUFFIX = "}";

    final String prefix;

    final String suffix;

    final PlaceholderEval eval;

    public ParserContext(PlaceholderEval eval) {
        this(DEFAULT_PREFIX, DEFAULT_SUFFIX, eval);
    }

    public ParserContext(String prefix, String suffix, PlaceholderEval eval) {
        if (prefix == null || prefix.length() <= 0
                || suffix == null || suffix.length() <= 0) {
            throw new IllegalArgumentException("");
        }

        if (eval == null) {
            throw new IllegalArgumentException("");
        }

        this.prefix = prefix;
        this.suffix = suffix;
        this.eval = eval;
    }
}
