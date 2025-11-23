package com.scproject.fuzzy.membership;

public class TriangularMF  implements MembershipFunction {
    private double a, b, c;

    public TriangularMF(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double getMembership(double x) {
        if (x <= a || x >= c) return 0.0;
        if (x == b) return 1.0;
        if (x < b) return (x - a) / (b - a);
        return (c - x) / (c - b);
    }

    @Override
    public double[] getDefiningPoints() {
        return new double[]{a, b, c};
    }
}
