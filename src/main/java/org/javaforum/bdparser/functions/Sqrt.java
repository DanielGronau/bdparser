package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.CalcUtils;
import org.javaforum.bdparser.ParseException;
import org.javaforum.bdparser.token.Function;

import java.math.BigDecimal;
import java.math.MathContext;

public class Sqrt implements Function {

    private static MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    private static final BigDecimal HALF = new BigDecimal("0.5");

    @Override
    public boolean hasArity(int count) {
        return count == 1;
    }

    @Override
    public BigDecimal calculate(BigDecimal... values) {
        if (values[0].signum() == -1) {
            throw new ParseException("Can't calculate the square root of a negative number (" + values[0] + ")");
        }
        return CalcUtils.pow(values[0], HALF, MATH_CONTEXT);
    }

    @Override
    public String toString() {
        return "sqrt()";
    }

    public static MathContext getMathContext() {
        return MATH_CONTEXT;
    }

    public static void setMathContext(MathContext mathContext) {
        MATH_CONTEXT = mathContext;
    }
}
