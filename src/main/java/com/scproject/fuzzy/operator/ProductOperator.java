package com.scproject.fuzzy.operator;

public class ProductOperator implements FuzzyOperator {

    @Override
    public double apply(double a, double b) {
        return a * b;
    }
}
