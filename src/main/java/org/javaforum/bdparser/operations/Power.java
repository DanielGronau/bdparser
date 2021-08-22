package org.javaforum.bdparser.operations;

import org.javaforum.bdparser.CalcUtils;
import org.javaforum.bdparser.ParseException;
import org.javaforum.bdparser.token.Operation;

import java.math.BigDecimal;
import java.math.MathContext;

public class Power implements Operation {

    private static MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    @Override
    public int getPriority() {
        return 3;
    }

    public BigDecimal calculate(BigDecimal a, BigDecimal b) {
        return CalcUtils.pow(a, b, MATH_CONTEXT);
    }

    public static MathContext getMathContext() {
        return MATH_CONTEXT;
    }

    public static void setMathContext(MathContext mathContext) {
        MATH_CONTEXT = mathContext;
    }
}
