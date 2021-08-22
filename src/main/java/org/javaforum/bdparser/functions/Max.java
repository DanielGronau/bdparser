package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.token.Function;

import java.math.BigDecimal;
import java.util.Arrays;

public class Max implements Function {

    public BigDecimal calculate(BigDecimal... values) {
        return Arrays.stream(values).reduce(BigDecimal::max)
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean hasArity(int count) {
        return count > 0;
    }
}
