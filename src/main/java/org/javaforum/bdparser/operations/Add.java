package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.token.Operation;

import java.math.BigDecimal;

public class Add implements Operation {

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
}
