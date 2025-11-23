package com.scproject.fuzzy.operator;

import java.util.Arrays;


public class MinOperator implements FuzzyOperator {
    @Override
    public double apply(double a, double b) {
        return Math.min(a, b);
    }

    @Override
    public double apply(double[] values) {
        return Arrays.stream(values).min().orElse(0.0);
    }
}
