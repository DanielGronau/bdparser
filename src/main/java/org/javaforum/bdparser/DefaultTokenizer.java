package org.javaforum.bdparser;


import org.javaforum.bdparser.token.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Code adapted from
 * http://www.java-forum.org/allgemeines/12306-parser-fuer-mathematische-formeln.html
 * written by Benjamin "Beni" Sigg
 */
public class DefaultTokenizer implements Tokenizer {

    private Map<String, Operation> operations = new HashMap<String, Operation>();
    private Map<String, Function> functions = new HashMap<String, Function>();
    private Map<String, NumberToken> constants = new HashMap<String, NumberToken>();
    private List<Map<String, ? extends Token>> maps = new ArrayList<Map<String, ? extends Token>>();

    public DefaultTokenizer() {
        for (Operation op : Operation.values()) {
            addOperation(op);
        }

        for (Function function : Function.values()) {
            addFunction(function);
        }
        addConstant("pi", BigDecimal.valueOf(Math.PI));
        addConstant("e", BigDecimal.valueOf(Math.E));

        maps.add(constants);
        maps.add(functions);
        maps.add(operations);
    }

    public void addOperation(Operation operation) {
        operations.put(operation.getName(), operation);
    }

    public void addFunction(Function function) {
        functions.put(function.getName(), function);
    }

    public void addConstant(String name, BigDecimal value) {
        constants.put(name, new NumberToken(value));
    }

    public List<Token> tokenize(String formula) {

        int offset = 0;
        int length = formula.length();
        List<Token> parts = new ArrayList<Token>();

        while (offset < length) {
            char current = formula.charAt(offset);

            if (Character.isWhitespace(current)) {
                offset++;
            } else if (readCharToken(current, parts)) {
                offset++;
            } else if (Character.isDigit(current) || current == '.') {
                offset = readNumberToken(current, offset, formula, parts);
            } else {

                int tokenLength = 0;
                for(Map<String, ? extends Token> map : maps) {
                   tokenLength = readOtherToken(map, offset, formula, parts);
                    if (tokenLength > 0) {
                        break;
                    }
                }
                if (tokenLength == 0) {
                    throw new IllegalArgumentException("Something is wrong with this formula");
                }
                offset += tokenLength;
            }
        }
        return parts;
    }

    private boolean readCharToken(char ch, List<Token> parts) {
        switch (ch) {
            case '(':
                parts.add(CharToken.OPEN);
                return true;
            case ')':
                parts.add(CharToken.CLOSE);
                return true;
            case ',':
                parts.add(CharToken.SEPARATOR);
                return true;
            default:
                return false;
        }
    }

    private int readNumberToken(char current, int offset, String formula, List<Token> parts) {
        int end = offset + 1;
        boolean pointSeen = current == '.';

        while (end < formula.length()) {
            char next = formula.charAt(end);
            if (Character.isDigit(next)) {
                end++;
            } else if (next == '.' && !pointSeen) {
                pointSeen = true;
                end++;
            } else {
                break;
            }
        }
        parts.add(new NumberToken(new BigDecimal(formula.substring(offset, end))));
        return end;
    }

    private int readOtherToken(Map<String,? extends Token> map, int offset, String formula, List<Token> parts) {
        for (String check : map.keySet()) {
            if (formula.startsWith(check, offset) ) {
                parts.add(map.get(check));
                return check.length();
            }
        }
        return 0;
    }

    public List<String> getFunctionNames() {
        List<String> list = new ArrayList<String>(functions.keySet());
        Collections.sort(list);
        return list;
    }

}
