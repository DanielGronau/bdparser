package org.bdparser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private static final BigDecimal EPSILON = new BigDecimal("0.000000000001");

    @Test
    public void testParse() {
        assertCloseTo("7", "7");
        assertCloseTo("7", "7.000");
        assertCloseTo("-7", "-7");
        assertCloseTo("0.2", ".2");
        assertCloseTo("3", "1+2");
        assertCloseTo("6", "1+2+3");
        assertCloseTo("-1", "1-2");
        assertCloseTo("2", "1*2");
        assertCloseTo("22", "2+4*5");
        assertCloseTo("40", "2*4*5");
        assertCloseTo("30", "(2+4)*5");
        assertCloseTo("5", "(((5)))");
        assertCloseTo("0.75", "1 +- .25");
        assertCloseTo("0.25", "1 * + .25");
        assertCloseTo("1", "1 ^ 1");
        assertCloseTo("1", "17 ^ 0");
        assertCloseTo("4", "16 ^ 0.5");
        assertCloseTo("5", "125 ^ (1/3)");
        assertCloseTo("12.25", "3.5 ^ 2");
        assertCloseTo("0.2", "5 ^ -1");
        assertCloseTo("0.5", "1/2");
        assertCloseTo("12", "6 * (-4 / -2)");
        assertCloseTo("0.3333333333333333", "1/3");
        assertCloseTo("6", "13 % 7");
        assertCloseTo("0.27", "6.27 % 0.5");
    }

    @Test
    public void functions() {
        assertCloseTo("1", "min(1)");
        assertCloseTo("1", "min( 11, 1, 3 )");
        assertCloseTo("11", "max( 11, 1, 3 )");
        assertCloseTo("3", "abs (-3)");
        assertCloseTo("2", "2*abs(-3)-4");
        assertCloseTo("12", "sqrt(144)");
        assertCloseTo("1.41421356237309504880", "sqrt( 2 )");
        assertCloseTo("3", "log( 1000 )");
        assertCloseTo("3", "ln( 1000 ) / ln(10)");
        assertCloseTo("1", "ln( " + Math.E + " )");
    }

    @Test
    public void constants() {
        assertCloseTo("" + CalcUtils.PI, "pi");
        assertCloseTo("" + CalcUtils.E, "e");
        assertCloseTo("" + CalcUtils.PI.multiply(BigDecimal.valueOf(2)), "2*pi");
    }

    @Test
    public void divisionByZero() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("6/0.00"));
        assertEquals("Can't divide by zero", ex.getMessage());
    }

    @Test
    public void divisionByZero_Remainder() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("6 % 0.00"));
        assertEquals("Can't divide by zero", ex.getMessage());
    }

    @Test
    public void incompleteOperation() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3+5+"));
        assertEquals("Formula can't be resolved", ex.getMessage());
    }

    @Test
    public void missingOperator() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3 5"));
        assertEquals("Formula can't be resolved", ex.getMessage());
    }

    @Test
    public void testWrongArity() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("abs(3,5)"));
        assertEquals("Wrong number of arguments for function abs(), found: 2", ex.getMessage());
    }

    @Test
    public void unbalancedParentheses1() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3 + (4"));
        assertEquals("Closing parenthesis is missing", ex.getMessage());
    }

    @Test
    public void unbalancedParentheses2() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3 + )4"));
        assertEquals("Closing parenthesis without opening parenthesis", ex.getMessage());
    }

    @Test
    public void unbalancedParentheses3() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3 + )4("));
        assertEquals("Closing parenthesis without opening parenthesis", ex.getMessage());
    }

    @Test
    public void unbalancedParentheses4() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("(3 + )4)"));
        assertEquals("Closing parenthesis without opening parenthesis", ex.getMessage());
    }

    @Test
    public void emptyParentheses() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3 + () 5"));
        assertEquals("Missing expression, e.g. empty parentheses", ex.getMessage());
    }

    @Test
    public void multipleDots() {
        Throwable ex = assertThrows(ParseException.class, () -> new Parser().parse("3.5."));
        assertEquals("Couldn't tokenize number .", ex.getMessage());
    }

    private void assertCloseTo(String expected, String actual) {
        BigDecimal result = new Parser().parse(actual);
        assertTrue(new BigDecimal(expected).subtract(result).abs().compareTo(EPSILON) < 0, 
                "Result " + result + " of formula " + actual + " should be close to " + expected);
    }

}
