package org.bdparser.functions;

import org.bdparser.CalcUtils;
import org.bdparser.token.FunctionToken;

import java.math.BigDecimal;

public class Ln implements FunctionToken {

    @Override
    public boolean hasArity(int count) {
        return count == 1;
    }

    @Override
    public BigDecimal calculate(BigDecimal... values) {
        return CalcUtils.log(CalcUtils.E, values[0]);
    }

    @Override
    public String toString() {
        return "ln()";
    }
}
