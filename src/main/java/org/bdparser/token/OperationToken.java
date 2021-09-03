package org.bdparser.token;

import java.math.BigDecimal;


/**
 * Code adapted from
 * http://www.java-forum.org/allgemeines/12306-parser-fuer-mathematische-formeln.html
 * written by Benjamin "Beni" Sigg
 */
public interface OperationToken extends Token {

    int getPriority();

    BigDecimal calculate(BigDecimal a, BigDecimal b);

    /**
     * Used to check if a unary version with an "invisible zero" is possible, so expressions like -1 are valid.
     * @return false by default
     */
    default boolean unaryFix() {
        return false;
    }
}
