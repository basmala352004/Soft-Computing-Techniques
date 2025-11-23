package com.scproject.fuzzy.core;

import com.scproject.fuzzy.aggregation.AggregationMethod;
import com.scproject.fuzzy.defuzzification.DefuzzificationMethod;
import com.scproject.fuzzy.inference.InferenceEngine;
import com.scproject.fuzzy.operator.FuzzyOperator;
import com.scproject.fuzzy.rulebase.RuleBase;

import java.util.*;


public class FuzzySystem {
    private List<FuzzyVariable> inputs;      // At least 2 inputs
    private FuzzyVariable output;            // One output
    private RuleBase ruleBase;
    private InferenceEngine inferenceEngine;      // Mamdani
    private FuzzyOperator andOperator;            // min or product
    private FuzzyOperator orOperator;             // max or algebraic sum
    private AggregationMethod aggregation;        // max (union)
    private DefuzzificationMethod defuzzifier;    // centroid, weighted avg, etc.

    // Pipeline: fuzzify → infer → aggregate → defuzzify
    public double evaluate(Map<String, Double> crispInputs) {
        return 0;
    }

    // For debugging/visualization
    public Map<String, Map<String, Double>> getFuzzifiedInputs() {
        return null;
    }

    public Map<FuzzyRule, Double> getRuleStrengths() {
        return null;
    }

    public FuzzySet getAggregatedOutput() {
        return null;
    }
}
