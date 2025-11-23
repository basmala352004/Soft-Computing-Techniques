package com.scproject.fuzzy.defuzzification;

import com.scproject.fuzzy.core.FuzzySet;


public class MaxMembershipDefuzz implements DefuzzificationMethod {

    private int steps;

    public MaxMembershipDefuzz() {
        this.steps = 1000;
    }

    public MaxMembershipDefuzz(int steps) {
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
        double maxMembership = 0.0;
        double xAtMax = (minRange + maxRange) / 2.0;


        for (int i = 0; i <= steps; i++) {
            double x = minRange + i * stepSize;
            double membership = aggregatedSet.getMembership(x);

            if (membership > maxMembership) {
                maxMembership = membership;
                xAtMax = x;
            }
        }

        if (maxMembership == 0.0) {
            return (minRange + maxRange) / 2.0;
        }

        return xAtMax;
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