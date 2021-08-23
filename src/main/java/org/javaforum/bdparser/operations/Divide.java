package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.ParseException;
import org.javaforum.bdparser.token.OperationToken;

import java.math.BigDecimal;
import java.math.MathContext;

public class Divide implements OperationToken {

    private static MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    @Override
    public int getPriority() {
        return 2;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        if (b.signum() == 0) {
            throw new ParseException("Can't divide by zero");
        }
        return a.divide(b, MATH_CONTEXT);
    }

    public static void setMathContext(MathContext mathContext) {
        MATH_CONTEXT = mathContext;
    }

    public static MathContext getMathContext() {
        return MATH_CONTEXT;
    }

    @Override
    public String toString() {
        return "Divide";
    }
}
