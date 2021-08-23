package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.token.OperationToken;

import java.math.BigDecimal;

public class Subtract implements OperationToken {

    @Override
    public int getPriority() {
        return 1;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    @Override
    public boolean unaryFix() {
        return true;
    }

    @Override
    public String toString() {
        return "Subtract";
    }
}
