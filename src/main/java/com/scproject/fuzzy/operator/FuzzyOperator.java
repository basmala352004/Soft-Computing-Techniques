package com.scproject.fuzzy.operator;

public interface FuzzyOperator {
    double apply(double a, double b);
    default double apply(double[] values) {
        if (values == null || values.length == 0) return 0.0;
        double result = values[0];
        for (int i = 1; i < values.length; i++) {
            result = apply(result, values[i]);
        }
        return result;
    }
}
