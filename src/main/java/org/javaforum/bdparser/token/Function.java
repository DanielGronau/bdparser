package org.javaforum.bdparser.token;

import java.math.BigDecimal;

/**
 * Code adapted from
 * http://www.java-forum.org/allgemeines/12306-parser-fuer-mathematische-formeln.html
 * written by Benjamin "Beni" Sigg
 */
public enum Function implements Token {

    MIN("min") {
        public BigDecimal calculate(BigDecimal... values) {
            BigDecimal result = null;
            for (BigDecimal bd : values) {
                result = result == null ? bd : result.min(bd);
            }
            return result;
        }

        public boolean hasArity(int count) {
            return count > 0;
        }
    },

    MAX("max") {
        public BigDecimal calculate(BigDecimal... values) {
            BigDecimal result = null;
            for (BigDecimal bd : values) {
                result = result == null ? bd : result.max(bd);
            }
            return result;
        }

        public boolean hasArity(int count) {
            return count > 0;
        }
    },


    ABS("abs") {
        public BigDecimal calculate(BigDecimal... values) {
            return values[0].abs();
        }

        public boolean hasArity(int count) {
            return count == 1;
        }
    };

    private final String name;

    Function(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean hasArity(int count);

    public abstract BigDecimal calculate(BigDecimal... values);

}
