package org.bdparser.operations;

import org.bdparser.token.OperationToken;

import java.math.BigDecimal;

public class Add implements OperationToken {

    @Override
    public int getPriority() {
        return 1;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    @Override
    public boolean unaryFix() {
        return true;
    }

    @Override
    public String toString() {
        return "Add";
    }
}
