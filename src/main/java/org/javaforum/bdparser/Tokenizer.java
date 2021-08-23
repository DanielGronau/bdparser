package org.javaforum.bdparser;

import org.javaforum.bdparser.token.FunctionToken;
import org.javaforum.bdparser.token.OperationToken;
import org.javaforum.bdparser.token.Token;

import java.math.BigDecimal;
import java.util.List;


public interface Tokenizer {

    List<Token> tokenize(String formula);

    void addOperation(String name, OperationToken operation);

    void addFunction(String name , FunctionToken function);

    void addConstant(String name, BigDecimal value);

}
