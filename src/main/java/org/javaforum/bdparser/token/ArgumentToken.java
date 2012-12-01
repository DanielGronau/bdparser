package org.javaforum.bdparser.token;

import java.math.BigDecimal;

public class ArgumentToken implements Token {

    private BigDecimal[] bigDecimals;

    public ArgumentToken(BigDecimal... bigDecimals) {
        assert bigDecimals != null;
        this.bigDecimals = bigDecimals;
    }

    public BigDecimal[] getArguments() {
        return bigDecimals;
    }

}
