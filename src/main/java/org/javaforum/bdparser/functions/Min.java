package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.token.Function;

import java.math.BigDecimal;
import java.util.Arrays;

public class Min implements Function {

    public BigDecimal calculate(BigDecimal... values) {
        return Arrays.stream(values).reduce(BigDecimal::min)
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean hasArity(int count) {
        return count > 0;
    }
}
