package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.token.Operation;

import java.math.BigDecimal;

public class Multiply implements Operation {

    @Override
    public int getPriority() {
        return 2;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }
}
