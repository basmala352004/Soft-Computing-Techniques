package com.scproject.fuzzy.defuzzification;

import com.scproject.fuzzy.core.FuzzySet;


public class MeanOfMaximumDefuzz implements DefuzzificationMethod {

    private int steps;
    private double tolerance;


    public MeanOfMaximumDefuzz() {
        this.steps = 1000;
        this.tolerance = 0.001; // Values within 0.001 of max are considered "at max"
    }


    public MeanOfMaximumDefuzz(int steps, double tolerance) {
        if (steps < 10) {
            throw new IllegalArgumentException("Steps must be at least 10");
        }
        if (tolerance < 0) {
            throw new IllegalArgumentException("Tolerance must be non-negative");
        }
        this.steps = steps;
        this.tolerance = tolerance;
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

        // First pass: find the maximum membership value
        double maxMembership = 0.0;
        for (int i = 0; i <= steps; i++) {
            double x = minRange + i * stepSize;
            double membership = aggregatedSet.getMembership(x);
            if (membership > maxMembership) {
                maxMembership = membership;
            }
        }

        // If no membership found, return midpoint
        if (maxMembership == 0.0 || Math.abs(maxMembership) < 1e-10) {
            return (minRange + maxRange) / 2.0;
        }

        // Second pass: collect all x values where membership is at or near maximum
        double sumX = 0.0;
        int count = 0;

        for (int i = 0; i <= steps; i++) {
            double x = minRange + i * stepSize;
            double membership = aggregatedSet.getMembership(x);

            if (Math.abs(membership - maxMembership) <= tolerance) {
                sumX += x;
                count++;
            }
        }

        if (count > 0) {
            return sumX / count;
        } else {
            return (minRange + maxRange) / 2.0;
        }
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

    public double getTolerance() {
        return tolerance;
    }


    public void setTolerance(double tolerance) {
        if (tolerance < 0) {
            throw new IllegalArgumentException("Tolerance must be non-negative");
        }
        this.tolerance = tolerance;
    }
}