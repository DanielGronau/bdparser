package org.javaforum.bdparser;

public class ParseException extends RuntimeException {

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Exception ex) {
        super(message, ex);
    }
}
