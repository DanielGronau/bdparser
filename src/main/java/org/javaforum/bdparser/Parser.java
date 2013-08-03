package org.javaforum.bdparser;


import org.javaforum.bdparser.token.*;

import java.math.BigDecimal;
import java.util.List;

import static org.javaforum.bdparser.token.CharToken.*;

/**
 * Code adapted from
 * http://www.java-forum.org/allgemeines/12306-parser-fuer-mathematische-formeln.html
 * written by Benjamin "Beni" Sigg
 */
public class Parser {

    private final Tokenizer tokenizer;
    private final NumberToken ZERO = new NumberToken(BigDecimal.ZERO);

    public Parser() {
        this(new DefaultTokenizer());
    }

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public BigDecimal parse(String formula) {

        List<Token> tokens = tokenizer.tokenize(formula);

        checkBalance(tokens);

        while (tokens.size() > 1) {
            parseFrom(tokens, 0);
        }

        if (tokens.isEmpty()) {
            throw new ParseException("Empty formula");
        }
        for (NumberToken numberToken : Tokens.number(tokens.get(0))) {
            return numberToken.getNumber();
        }
        throw new AssertionError("Got no result"); //this would be a real WTF that should not happen
    }

    private void checkBalance(List<Token> tokens) {
        int count = 0;
        for (Token token : tokens) {
            if (token == OPEN) {
                count++;
            } else if (token == CLOSE) {
                count--;
            }
            if (count < 0) {
                throw new ParseException("Closing parenthesis without opening parenthesis");
            }
            if (token == SEPARATOR && count == 0) {
                throw new ParseException("Comma outside of parenthesis");
            }
        }

        if (count != 0) {
            throw new ParseException(count > 0
                    ? "Closing parenthesis is missing"
                    : "Too many closing parentheses");
        }
    }

    private void parseFrom(List<Token> formula, int offset) {

        if (formula.get(offset) == OPEN) {
            formula.remove(offset);
        }

        for (Operation op : Tokens.operation(formula.get(offset))) {
            if (op.getPriority() == 1) {
                formula.add(offset, ZERO);
            }
        }

        int begin = offset;
        int length = 0;

        boolean done = false;

        while (begin + length < formula.size()) {
            Token end = formula.get(begin + length);
            if (end == OPEN) {
                parseFrom(formula, begin + length);
            } else if (end == CLOSE || end == SEPARATOR) {

                if (length == 0) {
                    throw new ParseException("Missing expression, e.g. empty parentheses");
                }

                parseFromTo(formula, begin, length);
                formula.remove(begin + 1);
                begin++;
                length = 0;

                if (end == CLOSE) {
                    done = true;
                    break;
                }
            } else {
                length++;
            }
        }

        if (!done && begin + length == formula.size()) {
            parseFromTo(formula, begin, length);
            begin++;
        }

        if (offset + 1 != begin) {
            BigDecimal[] value = new BigDecimal[begin - offset];
            for (int i = begin - 1; i >= offset; i--) {
                value[i - offset] = ((NumberToken) formula.remove(i)).getNumber();
                begin--;
            }
            formula.add(offset, new ArgumentToken(value));
        }
    }

    private void parseFromTo(List<Token> formula, int offset, int length) {
        length = evalFunctions(formula, offset, length);


        while (length > 1) {
            int current = length;

            int priority = Integer.MIN_VALUE;
            for (int i = offset; i < offset + length; i++) {
                for (Operation op : Tokens.operation(formula.get(i))) {
                    priority = Math.max(priority, op.getPriority());
                }
            }

            for (int i = offset + 1; i < offset + length - 1; i++) {
                for (Operation operation : Tokens.operation(formula.get(i))) {
                    if (operation.getPriority() == priority) {
                        Token left = formula.get(i - 1);
                        Token right = formula.get(i + 1);

                        //fix unary + and - if necessary
                        for (Operation rightOp : Tokens.operation(right)) {
                            if (rightOp.getPriority() == 1 &&
                                    formula.get(i + 2) instanceof NumberToken) {
                                formula.remove(i + 1);
                                right = new NumberToken(rightOp.calculate(BigDecimal.ZERO,
                                        ((NumberToken) formula.get(i + 1)).getNumber()));
                                formula.set(i + 1, right);
                                length -= 1;
                            }
                        }

                        if (!(left instanceof NumberToken) || !(right instanceof NumberToken)) {
                            throw new ParseException("Operator not between numbers");
                        }

                        formula.set(i, new NumberToken(operation.calculate(
                                ((NumberToken) left).getNumber(),
                                ((NumberToken) right).getNumber())));
                        formula.remove(i + 1);
                        formula.remove(i - 1);
                        i--;
                        length -= 2;
                    }
                }
            }

            if (length == current) {
                throw new ParseException("Formula can't be resolved");
            }
        }
    }

    //evaluate all functions, starting from the end
    //which automatically takes care of functions in functions
    private int evalFunctions(List<Token> formula, int offset, int length) {
        for (int i = offset + length - 2; i >= offset; i--) {
            for (Function function : Tokens.function(formula.get(i))) {
                if (i == offset ||
                        (!(formula.get(i - 1) instanceof NumberToken) &&
                                !(formula.get(i - 1) instanceof ArgumentToken))) {
                    Token arguments = formula.get(i + 1);
                    BigDecimal[] values = null;
                    for (ArgumentToken argument : Tokens.argument(arguments)) {
                        values = argument.getArguments();
                    }
                    for (NumberToken numberToken : Tokens.number(arguments)) {
                        values = new BigDecimal[]{numberToken.getNumber()};
                    }
                    if (values == null) {
                        throw new ParseException("Missing arguments for " + function + ", found: " + arguments);
                    }

                    if (!function.hasArity(values.length)) {
                        throw new ParseException("Wrong number of arguments for " + function + ", found: " +
                                values.length);
                    }

                    formula.remove(i + 1);
                    formula.set(i, new NumberToken(function.calculate(values)));
                    length--;
                }
            }
        }
        return length;
    }


}
