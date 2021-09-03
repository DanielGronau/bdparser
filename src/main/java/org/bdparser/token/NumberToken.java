package org.bdparser.token;

import java.math.BigDecimal;

public class NumberToken implements Token {

    private final BigDecimal bd;

    public NumberToken(BigDecimal bigDecimal) {
        assert bigDecimal != null;
        this.bd = bigDecimal;
    }

    public BigDecimal getNumber() {
        return bd;
    }

    @Override
    public String toString() {
        return bd.toString();
    }
}
