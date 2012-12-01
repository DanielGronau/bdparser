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
    private Map<String, BigDecimal> constants = new HashMap<String, BigDecimal>();

    public DefaultTokenizer() {
        for(Operation op : Operation.values()) {
          addOperation(op);
        }

        for(Function function : Function.values()) {
           addFunction(function);
        }
        addConstant("pi", BigDecimal.valueOf(Math.PI));
        addConstant("e", BigDecimal.valueOf(Math.E));
    }

    public void addOperation(Operation operation) {
        operations.put(operation.getName(), operation);
    }

    public void addFunction(Function function) {
        functions.put(function.getName(), function);
    }

    public void addConstant(String name, BigDecimal value) {
        constants.put(name, value);
    }

    public List<Token> tokenize(String formula) {

        int offset = 0;
        int length = formula.length();
        List<Token> parts = new ArrayList<Token>();

        while (offset < length) {
            char current = formula.charAt(offset);

            if (!Character.isWhitespace(current)) {
                int ci = "(),".indexOf(current);
                if (ci != -1) {
                    parts.add(CharToken.values()[ci]);
                    offset++;
                } else if (Character.isDigit(current) || current == '.') {
                    int end = offset + 1;
                    boolean pointSeen = current == '.';

                    while (end < length) {
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
                    offset = end;
                } else {

                    int bestLength = 0;
                    String best = null;

                    for (String check : constants.keySet()) {
                        if (formula.startsWith(check, offset) && check.length() > bestLength) {
                            bestLength = check.length();
                            best = check;
                            parts.add(new NumberToken(constants.get(check)));
                            break;
                        }
                    }
                    if (best == null) {
                        for (String check : functions.keySet()) {
                            if (formula.startsWith(check, offset) && check.length() > bestLength) {
                                bestLength = check.length();
                                best = check;
                                parts.add(functions.get(check));
                                break;
                            }
                        }
                    }

                    if (best == null) {
                        for (String check : operations.keySet()) {
                            if (formula.startsWith(check, offset) && check.length() > bestLength) {
                                bestLength = check.length();
                                best = check;
                                parts.add(operations.get(check));
                                break;
                            }
                        }
                    }

                    if (best == null) {
                        throw new IllegalArgumentException("Something is wrong with this formula");
                    }
                    offset += bestLength;
                }
            } else {
                offset++;
            }
        }
        return parts;
    }

    public List<String> getFunctionNames() {
        List<String> list = new ArrayList<String>(functions.keySet());
        Collections.sort(list);
        return list;
    }

}
