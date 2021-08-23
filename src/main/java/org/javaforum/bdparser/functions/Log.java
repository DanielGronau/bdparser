package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.CalcUtils;
import org.javaforum.bdparser.token.FunctionToken;

import java.math.BigDecimal;

public class Log implements FunctionToken {

    @Override
    public boolean hasArity(int count) {
        return count == 1;
    }

    @Override
    public BigDecimal calculate(BigDecimal... values) {
        return CalcUtils.log(BigDecimal.TEN, values[0]);
    }

    @Override
    public String toString() {
        return "log()";
    }
}
