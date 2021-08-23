package org.javaforum.bdparser.token;

import java.math.BigDecimal;

/**
 * Code adapted from
 * http://www.java-forum.org/allgemeines/12306-parser-fuer-mathematische-formeln.html
 * written by Benjamin "Beni" Sigg
 */
public interface FunctionToken extends Token {

    boolean hasArity(int count);

    BigDecimal calculate(BigDecimal... values);

}
