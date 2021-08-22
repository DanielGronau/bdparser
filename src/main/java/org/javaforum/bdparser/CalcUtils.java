package org.javaforum.bdparser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class CalcUtils {

    public static final BigDecimal PI = new BigDecimal("3.141592653589793238462");
    public static final BigDecimal E = new BigDecimal("2.7182818284590452353602");

    private CalcUtils() {
    }

    public static BigDecimal pow(BigDecimal a, BigDecimal b, MathContext ctx) {
        try {
            int signOf2 = b.signum();
            double dn1 = a.doubleValue();
            b = b.multiply(new BigDecimal(signOf2)); // n2 is now positive
            BigDecimal remainderOf2 = b.remainder(BigDecimal.ONE);
            BigDecimal n2IntPart = b.subtract(remainderOf2);
            BigDecimal intPow = a.pow(n2IntPart.intValueExact(), ctx);
            BigDecimal doublePow = BigDecimal.valueOf(Math.pow(dn1, remainderOf2.doubleValue()));
            BigDecimal result = intPow.multiply(doublePow);
            if (signOf2 == -1) {
                result = BigDecimal.ONE.divide(result, ctx);
            }
            return result;
        } catch (Exception e) {
            throw new ParseException("Problem while calculating the power", e);
        }
    }

    public static BigDecimal log(BigDecimal base, BigDecimal x) {
        BigDecimal result = BigDecimal.ZERO;

        BigDecimal input = new BigDecimal(x.toString());
        int decimalPlaces = 50;
        int scale = input.precision() + decimalPlaces;

        int maxite = 10000;
        int ite = 0;
        BigDecimal maxError_BigDecimal = new BigDecimal(BigInteger.ONE, decimalPlaces + 1);

        RoundingMode a_RoundingMode = RoundingMode.UP;

        BigDecimal two_BigDecimal = new BigDecimal("2");

        while (input.compareTo(base) > 0) {
            result = result.add(BigDecimal.ONE);
            input = input.divide(base, scale, a_RoundingMode);
        }

        BigDecimal fraction = new BigDecimal("0.5");
        input = input.multiply(input);
        BigDecimal resultplusfraction = result.add(fraction);
        while (((resultplusfraction).compareTo(result) > 0)
                && (input.compareTo(BigDecimal.ONE) > 0)) {
            if (input.compareTo(base) > 0) {
                input = input.divide(base, scale, a_RoundingMode);
                result = result.add(fraction);
            }
            input = input.multiply(input);
            fraction = fraction.divide(two_BigDecimal, scale, a_RoundingMode);
            resultplusfraction = result.add(fraction);
            if (fraction.abs().compareTo(maxError_BigDecimal) < 0) {
                break;
            }
            if (maxite == ite) {
                break;
            }
            ite++;
        }

        MathContext a_MathContext = new MathContext(((decimalPlaces - 1) + (result.precision() - result.scale())), RoundingMode.HALF_UP);
        BigDecimal roundedResult = result.round(a_MathContext);
        BigDecimal strippedRoundedResult = roundedResult.stripTrailingZeros();
        return strippedRoundedResult;
    }
}
