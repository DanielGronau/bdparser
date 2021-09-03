package org.bdparser;


import org.bdparser.functions.*;
import org.bdparser.operations.*;

/**
 * A tokenizer using all default operations, functions and constants defined in this library
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
