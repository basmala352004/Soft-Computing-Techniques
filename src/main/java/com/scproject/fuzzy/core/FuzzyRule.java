package com.scproject.fuzzy.core;

import com.scproject.fuzzy.operator.FuzzyOperator;

import java.util.*;


public class FuzzyRule {
    private Map<String, String> antecedents;      // variable → fuzzy set name
    private String consequentVariable;            // Output variable name
    private String consequentFuzzySet;            // Output fuzzy set name
    private String operator;                      // "AND" or "OR"
    private double weight;                        // Default: 1.0
    private boolean enabled;                      // Default: true

    public FuzzyRule(Map<String, String> antecedents, String consequentVariable, String consequentFuzzySet, String operator, double weight, boolean enabled) {
        this.antecedents = antecedents;
        this.consequentVariable = consequentVariable;
        this.consequentFuzzySet = consequentFuzzySet;
        this.operator = operator;
        this.weight = weight;
        this.enabled = enabled;
    }

    // Evaluate rule strength (firing strength)
    public double evaluateAntecedent(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            FuzzyOperator andOp,
            FuzzyOperator orOp
    ){
        return 0;
    }

    public void setAntecedents(Map<String, String> antecedents) {
        this.antecedents = antecedents;
    }

    public void setConsequentVariable(String consequentVariable) {
        this.consequentVariable = consequentVariable;
    }

    public void setConsequentFuzzySet(String consequentFuzzySet) {
        this.consequentFuzzySet = consequentFuzzySet;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, String> getAntecedents() {
        return antecedents;
    }

    public String getConsequentVariable() {
        return consequentVariable;
    }

    public String getConsequentFuzzySet() {
        return consequentFuzzySet;
    }

    public double getWeight() {
        return weight;
    }

    public String getOperator() {
        return operator;
    }

    public boolean isEnabled() {
        return enabled;
    }
}