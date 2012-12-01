package org.javaforum.bdparser;

import org.javaforum.bdparser.token.Function;
import org.javaforum.bdparser.token.Operation;
import org.javaforum.bdparser.token.Token;

import java.math.BigDecimal;

import java.util.List;


public interface Tokenizer {

  public List<Token> tokenize(String formula);

  public void addOperation(Operation operation);

  public void addFunction(Function function);

  public void addConstant(String name, BigDecimal value);

}
