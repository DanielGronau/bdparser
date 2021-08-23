package org.javaforum.bdparser.token;

import java.util.Optional;

public enum Tokens {

    ;

    @SuppressWarnings("unchecked")
    private static <T extends Token> Optional<T> optional(Token token, Class<T> clazz) {
        return clazz.isAssignableFrom(token.getClass())
                ? Optional.of((T) token)
                : Optional.empty();
    }

    public static Optional<NumberToken> number(Token token) {
        return optional(token, NumberToken.class);
    }

    public static Optional<ArgumentToken> argument(Token token) {
        return optional(token, ArgumentToken.class);
    }

    public static Optional<OperationToken> operation(Token token) {
        return optional(token, OperationToken.class);
    }

    public static Optional<FunctionToken> function(Token token) {
        return optional(token, FunctionToken.class);
    }

}
