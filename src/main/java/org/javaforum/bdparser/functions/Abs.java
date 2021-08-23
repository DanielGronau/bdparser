package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.token.FunctionToken;

import java.math.BigDecimal;

public class Abs implements FunctionToken {

    public BigDecimal calculate(BigDecimal... values) {
        return values[0].abs();
    }

    public boolean hasArity(int count) {
        return count == 1;
    }

    @Override
    public String toString() {
        return "abs()";
    }
}
