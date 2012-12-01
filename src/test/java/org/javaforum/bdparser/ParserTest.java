package org.javaforum.bdparser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ParserTest {
    public ParserTest() {
    }

    @Test
    public void testParse() {
        Parser parser = new Parser();
        assertEquals("7", parser.parse("7").toString());
        assertEquals("7.000", parser.parse("7.000").toString());
        assertEquals("-7", parser.parse("-7").toString());
        assertEquals("3", parser.parse("1+2").toString());
        assertEquals("6", parser.parse("1+2+3").toString());
        assertEquals("-1", parser.parse("1-2").toString());
        assertEquals("2", parser.parse("1*2").toString());
        assertEquals("22", parser.parse("2+4*5").toString());
        assertEquals("40", parser.parse("2*4*5").toString());
        assertEquals("30", parser.parse("(2+4)*5").toString());
        assertEquals("5", parser.parse("(((5)))").toString());
        assertEquals("0.75", parser.parse("1 +- .25").toString());
        assertEquals("0.25", parser.parse("1 * + .25").toString());
        assertEquals("1", parser.parse("1 ^ 1").toString());
        assertEquals("1", parser.parse("17 ^ 0").toString());
        assertEquals("4", parser.parse("16 ^ 0.5").toString());
        assertEquals("5", parser.parse("125 ^ (1/3)").toString());
        assertEquals("12.25", parser.parse("3.5 ^ 2").toString());
        assertEquals("0.2000000000000000", parser.parse("5 ^ -1").toString());
        assertEquals("0.5000000000000000", parser.parse("1/2").toString());
        assertEquals("12.0000000000000000", parser.parse("6 * (-4 / -2)").toString());
        assertEquals("0.3333333333333333", parser.parse("1/3").toString());
    }

    @Test
    public void testFunctions() {
        Parser parser = new Parser();
        assertEquals("1", parser.parse("min(1)").toString());
        assertEquals("1", parser.parse("min( 11, 1, 3 )").toString());
        assertEquals("11", parser.parse("max( 11, 1, 3 )").toString());
        assertEquals("13", parser.parse("max( abs(-11), 13*min(1, 3 ))").toString());
        assertEquals("3", parser.parse("abs (-3)").toString());
        assertEquals("2", parser.parse("2*abs(-3)-4").toString());
    }

    @Test
    public void testConstants() {
        Parser parser = new Parser();
        assertEquals("" + Math.PI, parser.parse("pi").toString());
        assertEquals("" + Math.E, parser.parse("e").toString());
        assertEquals("" + 2 * Math.PI, parser.parse("2*pi").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompleteOperation() {
        Parser parser = new Parser();
        parser.parse("3+5+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingOperator() {
        Parser parser = new Parser();
        parser.parse("3 5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongArity() {
        Parser parser = new Parser();
        parser.parse("abs(3,5)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbalancedParentheses1() {
        Parser parser = new Parser();
        parser.parse("3 + (4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbalancedParentheses2() {
        Parser parser = new Parser();
        parser.parse("3 + )4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbalancedParentheses3() {
        Parser parser = new Parser();
        parser.parse("3 + )4(");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unbalancedParentheses4() {
        Parser parser = new Parser();
        parser.parse("(3 + )4)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyParentheses() {
        Parser parser = new Parser();
        parser.parse("3 + () 5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void multipleDots() {
        Parser parser = new Parser();
        parser.parse("3.5.");
    }

}
