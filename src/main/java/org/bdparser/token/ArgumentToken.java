package org.bdparser.token;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public class ArgumentToken implements Token {

    private final BigDecimal[] bigDecimals;

    public ArgumentToken(BigDecimal... bigDecimals) {
        this.bigDecimals = requireNonNull(bigDecimals, "Argument 'bigDecimals' must be not null");
    }

    public BigDecimal[] getArguments() {
        return bigDecimals;
    }

    @Override
    public String toString() {
        return "args" + Arrays.toString(bigDecimals);
    }
}
