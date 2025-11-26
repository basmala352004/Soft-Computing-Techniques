package com.scproject.fuzzy.core;

import com.scproject.fuzzy.operator.FuzzyOperator;

import java.util.*;


public class FuzzyRule {
    private Map<String, String> antecedents;      // variable → fuzzy set name
    private String consequentVariable;            // Output variable name
    private String consequentFuzzySet;            // Output fuzzy set name
    private String operator;                      // "AND" or "OR"
    private double weight;                        // Default: 1.0
    private boolean enabled;
    private boolean isSugenoRule;
    private Double sugenoOutputValue;

    public FuzzyRule(Map<String, String> antecedents, String consequentVariable, String consequentFuzzySet, String operator, double weight, boolean enabled) {
        this.antecedents = antecedents;
        this.consequentVariable = consequentVariable;
        this.consequentFuzzySet = consequentFuzzySet;
        this.operator = operator;
        this.weight = weight;
        this.enabled = enabled;
    }

    public double evaluateAntecedent(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            FuzzyOperator andOp,
            FuzzyOperator orOp) {

        if (antecedents == null || antecedents.isEmpty()) {
            return 0.0;
        }

        List<Double> membershipValues = new ArrayList<>();

        for (Map.Entry<String, String> condition : antecedents.entrySet()) {
            String variableName = condition.getKey();
            String fuzzySetName = condition.getValue();

            if (!fuzzifiedInputs.containsKey(variableName)) {
                throw new IllegalArgumentException("Variable not found in inputs: " + variableName);
            }

            Map<String, Double> variableMemberships = fuzzifiedInputs.get(variableName);

            if (!variableMemberships.containsKey(fuzzySetName)) {
                throw new IllegalArgumentException("Fuzzy set not found: " + fuzzySetName);
            }

            double membership = variableMemberships.get(fuzzySetName);
            membershipValues.add(membership);
        }

        if (membershipValues.size() == 1) {
            return membershipValues.get(0);
        }

        double result = membershipValues.get(0);

        for (int i = 1; i < membershipValues.size(); i++) {
            if ("AND".equals(operator)) {
                result = andOp.apply(result, membershipValues.get(i));
            } else if ("OR".equals(operator)) {
                result = orOp.apply(result, membershipValues.get(i));
            }
        }

        return result;
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

    public boolean isSugenoRule() {return isSugenoRule;}

    public void setSugenoRule(boolean sugeno) {this.isSugenoRule = sugeno;}

    public Double getSugenoOutputValue() {return sugenoOutputValue;}

    public void setSugenoOutputValue(Double value) {this.sugenoOutputValue = value;}
}