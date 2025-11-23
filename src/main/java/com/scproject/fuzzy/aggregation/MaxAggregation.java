package com.scproject.fuzzy.aggregation;

import com.scproject.fuzzy.core.FuzzySet;
import com.scproject.fuzzy.membership.MembershipFunction;

import java.util.*;

/**
 * Max aggregation method (Union operation).
 * Combines multiple fuzzy sets by taking the maximum membership value
 * at each point in the universe of discourse.
 * This is the standard aggregation method used in Mamdani inference.
 */
public class MaxAggregation implements AggregationMethod {

    @Override
    public FuzzySet aggregate(Collection<FuzzySet> sets) {

        // Handle empty or null input
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

        // Merge all defining points from all sets
        SortedSet<Double> mergedPoints = new TreeSet<>();

        for (FuzzySet fs : sets) {
            double[] pts = fs.getMembershipFunction().getDefiningPoints();
            for (double p : pts) {
                mergedPoints.add(p);
            }
        }

        // Convert to array for the aggregated membership function
        double[] definingPoints = mergedPoints.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        // Create aggregated membership function that takes MAX at each point
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