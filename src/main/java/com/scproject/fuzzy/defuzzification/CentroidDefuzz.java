package com.scproject.fuzzy.defuzzification;

import com.scproject.fuzzy.core.FuzzySet;




public class CentroidDefuzz implements DefuzzificationMethod {



    private int steps;


    public CentroidDefuzz() {
        this.steps = 1000;
    }

    public CentroidDefuzz(int steps) {
        if (steps < 10) {
            throw new IllegalArgumentException("Steps must be at least 10");
        }
        this.steps = steps;
    }

    @Override
    public double defuzzify(FuzzySet aggregatedSet, double minRange, double maxRange) {
        if (aggregatedSet == null) {
            throw new IllegalArgumentException("Aggregated set cannot be null");
        }

        if (minRange >= maxRange) {
            throw new IllegalArgumentException("minRange must be less than maxRange");
        }

        double stepSize = (maxRange - minRange) / steps;
        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i <= steps; i++) {
            double x = minRange + i * stepSize;
            double membership = aggregatedSet.getMembership(x);

            numerator += x * membership * stepSize;
            denominator += membership * stepSize;
        }

        if (denominator == 0 || Math.abs(denominator) < 1e-10) {
            return (minRange + maxRange) / 2.0;
        }

        return numerator / denominator;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        if (steps < 10) {
            throw new IllegalArgumentException("Steps must be at least 10");
        }
        this.steps = steps;
    }
}
