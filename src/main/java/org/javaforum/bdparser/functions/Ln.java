package org.javaforum.bdparser.functions;

import org.javaforum.bdparser.CalcUtils;
import org.javaforum.bdparser.token.Function;

import java.math.BigDecimal;

public class Ln implements Function {

    @Override
    public boolean hasArity(int count) {
        return count == 1;
    }

    @Override
    public BigDecimal calculate(BigDecimal... values) {
        return CalcUtils.log(CalcUtils.E, values[0]);
    }
}
