package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.ParseException;
import org.javaforum.bdparser.token.Operation;

import java.math.BigDecimal;
import java.math.MathContext;

public class Remainder implements Operation {

    private static MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    @Override
    public int getPriority() {
        return 2;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        if (b.signum() == 0) {
            throw new ParseException("Can't divide by zero");
        }
        return a.remainder(b, MATH_CONTEXT);
    }

    public static void setMathContext(MathContext mathContext) {
        MATH_CONTEXT = mathContext;
    }

    public static MathContext getMathContext() {
        return MATH_CONTEXT;
    }
}
