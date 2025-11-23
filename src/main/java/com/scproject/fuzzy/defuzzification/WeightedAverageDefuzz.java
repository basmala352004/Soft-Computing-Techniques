package com.scproject.fuzzy.defuzzification;

import com.scproject.fuzzy.core.FuzzySet;


public class WeightedAverageDefuzz implements DefuzzificationMethod {


    public WeightedAverageDefuzz() {

    }


    @Override
    public double defuzzify(FuzzySet aggregatedSet, double minRange, double maxRange) {
        if (aggregatedSet == null) {
            throw new IllegalArgumentException("Aggregated set cannot be null");
        }

        if (minRange >= maxRange) {
            throw new IllegalArgumentException("minRange must be less than maxRange");
        }

        double[] definingPoints = aggregatedSet.getMembershipFunction().getDefiningPoints();

        if (definingPoints == null || definingPoints.length == 0) {
            return (minRange + maxRange) / 2.0;
        }

        double centroid = aggregatedSet.getCentroid();

        double maxMembership = 0.0;
        int samplePoints = 100;
        double step = (maxRange - minRange) / samplePoints;

        for (int i = 0; i <= samplePoints; i++) {
            double x = minRange + i * step;
            double membership = aggregatedSet.getMembership(x);
            if (membership > maxMembership) {
                maxMembership = membership;
            }
        }

        // If no membership, return midpoint
        if (maxMembership == 0.0 || Math.abs(maxMembership) < 1e-10) {
            return (minRange + maxRange) / 2.0;
        }

        return centroid;
    }


    public double defuzzifyWithCentroids(double[] centroids, double[] weights) {
        if (centroids == null || weights == null) {
            throw new IllegalArgumentException("Centroids and weights cannot be null");
        }

        if (centroids.length != weights.length) {
            throw new IllegalArgumentException("Centroids and weights must have same length");
        }

        if (centroids.length == 0) {
            throw new IllegalArgumentException("Must have at least one centroid");
        }

        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < centroids.length; i++) {
            numerator += centroids[i] * weights[i];
            denominator += weights[i];
        }

        if (denominator == 0 || Math.abs(denominator) < 1e-10) {
            // Return average of centroids if weights sum to zero
            double sum = 0;
            for (double c : centroids) {
                sum += c;
            }
            return sum / centroids.length;
        }

        return numerator / denominator;
    }
}