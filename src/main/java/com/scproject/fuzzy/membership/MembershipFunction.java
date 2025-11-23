package com.scproject.fuzzy.membership;

public interface MembershipFunction {
    double getMembership(double x);
    double[] getDefiningPoints();
}
