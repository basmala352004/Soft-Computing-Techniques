package com.scproject.fuzzy.membership;

public class TrapezoidalMF implements MembershipFunction{
    private double a, b, c, d;

    public TrapezoidalMF(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public double getMembership(double x) {
        if (x <= a || x >= d) return 0.0;
        if (x >= b && x <= c) return 1.0;
        if (x < b) return (x - a) / (b - a);
        return (d - x) / (d - c);
    }

    @Override
    public double[] getDefiningPoints() {
        return new double[]{a, b, c, d};
    }
}
