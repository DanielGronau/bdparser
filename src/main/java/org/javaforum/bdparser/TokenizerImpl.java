package org.javaforum.bdparser;

import org.javaforum.bdparser.token.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * An "empty" tokenizer without any operations, functions or constants
 */
public class TokenizerImpl implements Tokenizer {

    protected final Map<String, Operation> operations = new HashMap<String, Operation>();
    protected final Map<String, Function> functions = new HashMap<String, Function>();
    protected final Map<String, NumberToken> constants = new HashMap<String, NumberToken>();
    protected final List<Map<String, ? extends Token>> maps = new ArrayList<Map<String, ? extends Token>>();

    public void addOperation(String name, Operation operation) {
        operations.put(name, operation);
    }

    public void addFunction(String name, Function function) {
        functions.put(name, function);
    }

    public void addConstant(String name, BigDecimal value) {
        constants.put(name, new NumberToken(value));
    }

    public List<Token> tokenize(String formula) {

        int offset = 0;
        int length = formula.length();
        List<Token> parts = new ArrayList<>();

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
                for (Map<String, ? extends Token> map : maps) {
                    tokenLength = readOtherToken(map, offset, formula, parts);
                    if (tokenLength > 0) {
                        break;
                    }
                }
                if (tokenLength == 0) {
                    throw new ParseException("Could not tokenize formula [" + formula + "] at position " + offset);
                }
                offset += tokenLength;
            }
        }
        return parts;
    }

    private boolean readCharToken(char ch, List<Token> parts) {
        for (CharToken token : CharToken.values()) {
            if (token.getSymbol() == ch) {
                parts.add(token);
                return true;
            }
        }
        return false;
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
        try {
            BigDecimal result = new BigDecimal(formula.substring(offset, end));
            parts.add(new NumberToken(result));
        } catch (NumberFormatException ex) {
            throw new ParseException("Couldn't tokenize number " + formula.substring(offset, end), ex);
        }
        return end;
    }

    private int readOtherToken(Map<String, ? extends Token> map, int offset, String formula, List<Token> parts) {
        for (String check : map.keySet()) {
            if (formula.startsWith(check, offset)) {
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
