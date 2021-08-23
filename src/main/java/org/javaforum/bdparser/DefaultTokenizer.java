package org.javaforum.bdparser;


import org.javaforum.bdparser.functions.*;
import org.javaforum.bdparser.operations.*;

import java.math.BigDecimal;

/**
 * A tokenizer using all default operations, functions and constants defined in this library
 * <p>
 * Code adapted from
 * http://www.java-forum.org/allgemeines/12306-parser-fuer-mathematische-formeln.html
 * written by Benjamin "Beni" Sigg
 */
public class DefaultTokenizer extends TokenizerImpl {

    public DefaultTokenizer() {
        addOperation("+", new Add());
        addOperation("-", new Subtract());
        addOperation("*", new Multiply());
        addOperation("/", new Divide());
        addOperation("%", new Remainder());
        addOperation("^", new Power());

        addFunction("abs", new Abs());
        addFunction("min", new Min());
        addFunction("max", new Max());
        addFunction("sqrt", new Sqrt());
        addFunction("log", new Log());
        addFunction("ln", new Ln());

        addConstant("pi", CalcUtils.PI);
        addConstant("e", CalcUtils.E);
    }

}
