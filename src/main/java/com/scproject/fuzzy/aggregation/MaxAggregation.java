package com.scproject.fuzzy.aggregation;

import com.scproject.fuzzy.core.FuzzySet;
import com.scproject.fuzzy.membership.MembershipFunction;

import java.util.*;


public class MaxAggregation implements AggregationMethod {

    @Override
    public FuzzySet aggregate(Collection<FuzzySet> sets) {


        if (sets == null || sets.isEmpty()) {
            return new FuzzySet("empty", new MembershipFunction() {
                @Override
                public double getMembership(double x) {
                    return 0;
                }

                @Override
                public double[] getDefiningPoints() {
                    return new double[]{0, 1};
                }
            });
        }



        SortedSet<Double> mergedPoints = new TreeSet<>();

        for (FuzzySet fs : sets) {
            double[] pts = fs.getMembershipFunction().getDefiningPoints();
            for (double p : pts) {
                mergedPoints.add(p);
            }
        }


        double[] definingPoints = mergedPoints.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();


        MembershipFunction aggregatedMF = new MembershipFunction() {

            @Override
            public double getMembership(double x) {
                double max = 0;
                for (FuzzySet fs : sets) {
                    double μ = fs.getMembership(x);
                    if (μ > max) {
                        max = μ;
                    }
                }
                return max;
            }

            @Override
            public double[] getDefiningPoints() {
                return definingPoints;
            }
        };

        return new FuzzySet("aggregated", aggregatedMF);
    }
}