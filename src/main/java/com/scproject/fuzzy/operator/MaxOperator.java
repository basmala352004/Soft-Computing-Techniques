package com.scproject.fuzzy.operator;

import java.util.Arrays;


public class MaxOperator implements FuzzyOperator {
    @Override
    public double apply(double a, double b) {
        return Math.max(a, b);
    }

    @Override
    public double apply(double[] values) {
        return Arrays.stream(values).max().orElse(0.0);
    }
}
