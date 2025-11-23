package com.scproject.fuzzy.core;

import com.scproject.fuzzy.membership.MembershipFunction;

public class FuzzySet {
    private final String name;
    private final MembershipFunction membershipFunction;

    public FuzzySet(String name, MembershipFunction membershipFunction) {
        this.name = name;
        this.membershipFunction = membershipFunction;
    }

    public String getName() {
        return name;
    }

    public MembershipFunction getMembershipFunction() {
        return membershipFunction;
    }

    public double getMembership(double x) {
        return membershipFunction.getMembership(x);
    }

    /**
     * Computes centroid (center of gravity) numerically.
     * Uses defining points to determine domain.
     */
    public double getCentroid() {
        double[] pts = membershipFunction.getDefiningPoints();
        if (pts == null || pts.length < 2)
            return 0;

        double min = pts[0];
        double max = pts[pts.length - 1];

        int steps = 200;
        double step = (max - min) / steps;

        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i <= steps; i++) {
            double x = min + i * step;
            double μ = membershipFunction.getMembership(x);

            numerator += x * μ;
            denominator += μ;
        }

        if (denominator == 0)
            return (min + max) / 2.0;

        return numerator / denominator;
    }
}
