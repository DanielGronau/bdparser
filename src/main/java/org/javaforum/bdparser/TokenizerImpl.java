package org.javaforum.bdparser;

import org.javaforum.bdparser.token.*;

import java.math.BigDecimal;
import java.util.*;

import static java.util.function.Function.identity;

/**
 * An "empty" tokenizer without any operations, functions or constants
 */
public class TokenizerImpl implements Tokenizer {

    private final static Comparator<String> LONGEST_FIRST_COMPARATOR = Comparator.comparingInt(String::length).reversed()
            .thenComparing(identity());

    private final TreeMap<String, Token> treeMap = new TreeMap<>(LONGEST_FIRST_COMPARATOR);

    public void addOperation(String name, Operation operation) {
        treeMap.put(name, operation);
    }

    public void addFunction(String name, Function function) {
        treeMap.put(name, function);
    }

    public void addConstant(String name, BigDecimal value) {
        treeMap.put(name, new NumberToken(value));
    }

    {
        for (CharToken token : CharToken.values()) {
            treeMap.put(token.getSymbol(), token);
        }
    }

    public List<Token> tokenize(String formula) {
        int offset = 0;
        int length = formula.length();
        List<Token> parts = new ArrayList<>();

        while (offset < length) {
            char current = formula.charAt(offset);
            if (Character.isWhitespace(current)) {
                offset++;
            } else if (Character.isDigit(current) || current == '.') {
                offset = readNumberToken(current, offset, formula, parts);
            } else {
                int tokenLength = readOtherToken(treeMap, offset, formula, parts);
                if (tokenLength == 0) {
                    throw new ParseException("Could not tokenize formula [" + formula + "] at position " + offset);
                }
                offset += tokenLength;
            }
        }
        return parts;
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
}
