package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.token.FunctionToken;

import java.math.BigDecimal;
import java.util.Arrays;

public class Max implements FunctionToken {

    public BigDecimal calculate(BigDecimal... values) {
        return Arrays.stream(values).reduce(BigDecimal::max)
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean hasArity(int count) {
        return count > 0;
    }

    @Override
    public String toString() {
        return "max()";
    }
}
