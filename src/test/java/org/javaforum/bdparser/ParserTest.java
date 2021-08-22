package org.javaforum.bdparser;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ParserTest {

    private static final BigDecimal EPSILON = new BigDecimal("0.000000000001");

    @Test
    public void testParse() {
        assertCloseTo("7", "7");
        assertCloseTo("7", "7.000");
        assertCloseTo("-7", "-7");
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

    @Test(expected = ParseException.class)
    public void divisionByZero() {
        new Parser().parse("6/0.00");
    }

    @Test(expected = ParseException.class)
    public void divisionByZero_Remainder() {
        new Parser().parse("6 % 0.00");
    }

    @Test(expected = ParseException.class)
    public void incompleteOperation() {
        new Parser().parse("3+5+");
    }

    @Test(expected = ParseException.class)
    public void missingOperator() {
        new Parser().parse("3 5");
    }

    @Test(expected = ParseException.class)
    public void testWrongArity() {
        new Parser().parse("abs(3,5)");
    }

    @Test(expected = ParseException.class)
    public void unbalancedParentheses1() {
        new Parser().parse("3 + (4");
    }

    @Test(expected = ParseException.class)
    public void unbalancedParentheses2() {
        new Parser().parse("3 + )4");
    }

    @Test(expected = ParseException.class)
    public void unbalancedParentheses3() {
        new Parser().parse("3 + )4(");
    }

    @Test(expected = ParseException.class)
    public void unbalancedParentheses4() {
        new Parser().parse("(3 + )4)");
    }

    @Test(expected = ParseException.class)
    public void emptyParentheses() {
        new Parser().parse("3 + () 5");
    }

    @Test(expected = ParseException.class)
    public void multipleDots() {
        new Parser().parse("3.5.");
    }

    private void assertCloseTo(String expected, String actual) {
        BigDecimal result = new Parser().parse(actual);
        assertTrue("Result " + result + " of formula " + actual + " should be close to " + expected,
                new BigDecimal(expected).subtract(result).abs().compareTo(EPSILON) < 0);
    }

}
