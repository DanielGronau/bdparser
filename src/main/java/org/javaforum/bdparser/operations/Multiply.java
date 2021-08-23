package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.token.OperationToken;

import java.math.BigDecimal;

public class Multiply implements OperationToken {

    @Override
    public int getPriority() {
        return 2;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    @Override
    public String toString() {
        return "Multiply";
    }
}
