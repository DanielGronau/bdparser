package org.bdparser;

import org.bdparser.token.FunctionToken;
import org.bdparser.token.Token;
import org.bdparser.token.OperationToken;

import java.math.BigDecimal;
import java.util.List;


public interface Tokenizer {

    List<Token> tokenize(String formula);

    void addOperation(String name, OperationToken operation);

    void addFunction(String name , FunctionToken function);

    void addConstant(String name, BigDecimal value);

}
