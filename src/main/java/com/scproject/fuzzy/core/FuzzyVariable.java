package com.scproject.fuzzy.core;

import java.util.HashMap;
import java.util.Map;

public class FuzzyVariable {
    private String name;                          // e.g., "Temperature"
    private double minRange;                      // Universe of discourse min
    private double maxRange;                      // Universe of discourse max
    private Map<String, FuzzySet> fuzzySets;      // e.g., "Cold", "Warm", "Hot"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinRange() {
        return minRange;
    }

    public void setMinRange(double minRange) {
        this.minRange = minRange;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public Map<String, FuzzySet> getFuzzySets() {
        return fuzzySets;
    }

    public void setFuzzySets(Map<String, FuzzySet> fuzzySets) {
        this.fuzzySets = fuzzySets;
    }

    // Fuzzification: converts crisp value to membership degrees
//    public Map<String, Double> fuzzify(double crispValue){
//        return null;
//    }

    public Map<String, Double> fuzzify(double crispValue) {
        Map<String, Double> memberships = new HashMap<>();

        for (String setName : fuzzySets.keySet()) {
            FuzzySet set = fuzzySets.get(setName);
            double µ = set.getMembership(crispValue);
            memberships.put(setName, µ);
        }

        return memberships;
    }
}

