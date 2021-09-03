package org.bdparser;

import org.bdparser.token.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.util.function.Function.identity;

/**
 * An "empty" tokenizer without any operations, functions or constants
 */
public class TokenizerImpl implements Tokenizer {

    private final static Comparator<String> LONGEST_FIRST_COMPARATOR = Comparator.comparingInt(String::length).reversed()
            .thenComparing(identity());

    private final TreeMap<String, Token> treeMap = new TreeMap<>(LONGEST_FIRST_COMPARATOR);
    {
        for (CharToken token : CharToken.values()) {
            treeMap.put(token.getSymbol(), token);
        }
    }

    public void addOperation(String name, OperationToken operation) {
        treeMap.put(name, operation);
    }

    public void addFunction(String name, FunctionToken function) {
        treeMap.put(name, function);
    }

    public void addConstant(String name, BigDecimal value) {
        treeMap.put(name, new NumberToken(value));
    }

    public List<Token> tokenize(String formula) {
        State state = new State(formula);
        while (state.offset < formula.length()) {
            Optional<State> newState = attempt(
                    state,
                    this::readWhitespace,
                    this::readNumber,
                    this::readOther);
            int offset = state.offset;
            state = newState.orElseThrow(() ->
                    new ParseException("Could not tokenize formula [" + formula + "] at position " + offset));
        }
        return state.parts;
    }

    private Optional<State> readWhitespace(State state) {
        return Optional.of(Character.isWhitespace(state.current()))
                .filter(x -> x)
                .map(b -> {
                    state.inc(1);
                    return state;
                });
    }

    private Optional<State> readNumber(State state) {
        if (!Character.isDigit(state.current()) && state.current() != '.') {
            return Optional.empty();
        }

        int end = state.offset + 1;
        boolean pointSeen = state.current() == '.';

        while (end < state.formula.length()) {
            char next = state.formula.charAt(end);
            if (Character.isDigit(next)) {
                end++;
            } else if (next == '.' && !pointSeen) {
                pointSeen = true;
                end++;
            } else {
                break;
            }
        }
        String substring = state.formula.substring(state.offset, end);
        try {
            BigDecimal result = new BigDecimal(substring);
            return Optional.of(state
                    .add(new NumberToken(result))
                    .inc(substring.length()));
        } catch (NumberFormatException ex) {
            throw new ParseException("Couldn't tokenize number " + substring, ex);
        }
    }

    private Optional<State> readOther(State state) {
        return treeMap.keySet().stream()
                .filter(key -> state.formula.startsWith(key, state.offset))
                .findFirst()
                .map(key -> state.add(treeMap.get(key)).inc(key.length()));
    }

    @SafeVarargs
    private final Optional<State> attempt(State current, Function<State, Optional<State>>... fns) {
        return Arrays.stream(fns)
                .reduce(
                        Optional.empty(),
                        (newState, fn) -> newState.isPresent() ? newState : fn.apply(current),
                        (x, y) -> x);
    }

    private static class State {
        final String formula;
        int offset = 0;
        List<Token> parts = new ArrayList<>();

        State(String formula) {
            this.formula = formula;
        }

        char current() {
            return formula.charAt(offset);
        }

        State inc(int length) {
            offset += length;
            return this;
        }

        State add(Token token) {
            parts.add(token);
            return this;
        }
    }
}
