package org.bdparser.token;

public enum CharToken implements Token {
    OPEN("("),
    CLOSE(")"),
    SEPARATOR(",");

    private final String symbol;

    CharToken(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}

