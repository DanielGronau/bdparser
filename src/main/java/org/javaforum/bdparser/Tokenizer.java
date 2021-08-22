package org.javaforum.bdparser;

import org.javaforum.bdparser.token.Function;
import org.javaforum.bdparser.token.Operation;
import org.javaforum.bdparser.token.Token;

import java.math.BigDecimal;
import java.util.List;


public interface Tokenizer {

    List<Token> tokenize(String formula);

    void addOperation(String name, Operation operation);

    void addFunction(String name , Function function);

    void addConstant(String name, BigDecimal value);

}
