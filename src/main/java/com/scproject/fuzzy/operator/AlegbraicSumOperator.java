package com.scproject.fuzzy.operator;

public class AlegbraicSumOperator implements FuzzyOperator{

    @Override
    public double apply(double a, double b) {
        return a + b - (a * b);
    }

    @Override
    public double apply(double[] values) {
        double result = 0.0;
        for (double v : values) {
            result = apply(result, v);
        }
        return result;
    }
}
