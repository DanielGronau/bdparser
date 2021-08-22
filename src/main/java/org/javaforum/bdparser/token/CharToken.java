package org.javaforum.bdparser.token;

public enum CharToken implements Token {
    OPEN('('),
    CLOSE(')'),
    SEPARATOR(',');

    private final char symbol;

    CharToken(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

}

