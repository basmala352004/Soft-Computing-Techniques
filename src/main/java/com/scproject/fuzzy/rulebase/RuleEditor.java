package com.scproject.fuzzy.rulebase;

import com.scproject.fuzzy.core.FuzzyRule;

import java.util.Map;

/**
 * RuleEditor provides an API for creating, editing, enabling/disabling,
 * and managing fuzzy rules.
 *
 * This class serves as a high-level interface for rule management,
 * making it easier to build and modify rule bases programmatically.
 */
public class RuleEditor {

    private final RuleBase ruleBase;


    public RuleEditor(RuleBase ruleBase) {
        if (ruleBase == null) {
            throw new IllegalArgumentException("RuleBase cannot be null");
        }
        this.ruleBase = ruleBase;
    }

    public FuzzyRule createRule(
            Map<String, String> antecedents,
            String operator,
            String consequentVariable,
            String consequentFuzzySet) {

        return createRule(antecedents, operator, consequentVariable, consequentFuzzySet, 1.0);
    }

    public FuzzyRule createRule(
            Map<String, String> antecedents,
            String operator,
            String consequentVariable,
            String consequentFuzzySet,
            double weight) {

        if (antecedents == null || antecedents.isEmpty()) {
            throw new IllegalArgumentException("Antecedents cannot be null or empty");
        }

        if (operator == null || (!operator.equalsIgnoreCase("AND") && !operator.equalsIgnoreCase("OR"))) {
            throw new IllegalArgumentException("Operator must be 'AND' or 'OR'");
        }

        if (consequentVariable == null || consequentVariable.isEmpty()) {
            throw new IllegalArgumentException("Consequent variable cannot be null or empty");
        }

        if (consequentFuzzySet == null || consequentFuzzySet.isEmpty()) {
            throw new IllegalArgumentException("Consequent fuzzy set cannot be null or empty");
        }

        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }

        FuzzyRule rule = new FuzzyRule(
                antecedents,
                consequentVariable,
                consequentFuzzySet,
                operator.toUpperCase(),
                weight,
                true
        );

        ruleBase.addRule(rule);
        return rule;
    }


    public FuzzyRule createSimpleRule(
            String inputVariable,
            String inputFuzzySet,
            String outputVariable,
            String outputFuzzySet) {

        return createRule(
                Map.of(inputVariable, inputFuzzySet),
                "AND",
                outputVariable,
                outputFuzzySet
        );
    }

    public void editRuleAntecedents(int index, Map<String, String> newAntecedents) {
        validateIndex(index);

        if (newAntecedents == null || newAntecedents.isEmpty()) {
            throw new IllegalArgumentException("Antecedents cannot be null or empty");
        }

        FuzzyRule rule = ruleBase.getRule(index);
        rule.setAntecedents(newAntecedents);
    }


    public void editRuleConsequent(int index, String newConsequentVariable, String newConsequentFuzzySet) {
        validateIndex(index);

        if (newConsequentVariable == null || newConsequentVariable.isEmpty()) {
            throw new IllegalArgumentException("Consequent variable cannot be null or empty");
        }

        if (newConsequentFuzzySet == null || newConsequentFuzzySet.isEmpty()) {
            throw new IllegalArgumentException("Consequent fuzzy set cannot be null or empty");
        }

        FuzzyRule rule = ruleBase.getRule(index);
        rule.setConsequentVariable(newConsequentVariable);
        rule.setConsequentFuzzySet(newConsequentFuzzySet);
    }


    public void editRuleOperator(int index, String newOperator) {
        validateIndex(index);

        if (newOperator == null || (!newOperator.equalsIgnoreCase("AND") && !newOperator.equalsIgnoreCase("OR"))) {
            throw new IllegalArgumentException("Operator must be 'AND' or 'OR'");
        }

        FuzzyRule rule = ruleBase.getRule(index);
        rule.setOperator(newOperator.toUpperCase());
    }


    public void enableRule(int index, boolean enabled) {
        validateIndex(index);

        FuzzyRule rule = ruleBase.getRule(index);
        rule.setEnabled(enabled);
    }


    public void setRuleWeight(int index, double weight) {
        validateIndex(index);

        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }

        FuzzyRule rule = ruleBase.getRule(index);
        rule.setWeight(weight);
    }


    public void deleteRule(int index) {
        validateIndex(index);
        ruleBase.removeRule(index);
    }


    public FuzzyRule getRule(int index) {
        validateIndex(index);
        return ruleBase.getRule(index);
    }

    public int getRuleCount() {
        return ruleBase.size();
    }


    public int getActiveRuleCount() {
        return ruleBase.getActiveRules().size();
    }


    public void clearAllRules() {
        ruleBase.clear();
    }

    public void enableAllRules() {
        for (int i = 0; i < ruleBase.size(); i++) {
            ruleBase.getRule(i).setEnabled(true);
        }
    }

    public void disableAllRules() {
        for (int i = 0; i < ruleBase.size(); i++) {
            ruleBase.getRule(i).setEnabled(false);
        }
    }

    public void setWeightForAllRules(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }

        for (int i = 0; i < ruleBase.size(); i++) {
            ruleBase.getRule(i).setWeight(weight);
        }
    }


    public RuleBase getRuleBase() {
        return ruleBase;
    }


    private void validateIndex(int index) {
        if (index < 0 || index >= ruleBase.size()) {
            throw new IndexOutOfBoundsException(
                    "Rule index " + index + " is out of bounds. Rule base size: " + ruleBase.size()
            );
        }
    }

    public void printAllRules() {
        System.out.println("Rule Base (" + ruleBase.size() + " rules, " +
                getActiveRuleCount() + " active):");
        System.out.println("=".repeat(70));

        for (int i = 0; i < ruleBase.size(); i++) {
            FuzzyRule rule = ruleBase.getRule(i);
            System.out.printf("Rule %d: %s (Weight: %.2f) %s%n",
                    i,
                    rule.toString(),
                    rule.getWeight(),
                    rule.isEnabled() ? "[ENABLED]" : "[DISABLED]");
        }

        System.out.println("=".repeat(70));
    }
}