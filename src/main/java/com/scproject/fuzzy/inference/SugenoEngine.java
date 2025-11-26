package com.scproject.fuzzy.inference;

import com.scproject.fuzzy.core.FuzzyRule;
import com.scproject.fuzzy.core.FuzzySet;
import com.scproject.fuzzy.core.FuzzyVariable;
import com.scproject.fuzzy.membership.MembershipFunction;
import com.scproject.fuzzy.operator.FuzzyOperator;
import com.scproject.fuzzy.rulebase.RuleBase;

import java.util.*;

public class SugenoEngine implements InferenceEngine {

    private double outputMin = 0;
    private double outputMax = 100;

    @Override
    public FuzzySet inferAndAggregate(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyVariable outputVariable,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        if (outputVariable != null) {
            outputMin = outputVariable.getMinRange();
            outputMax = outputVariable.getMaxRange();
        }

        double output = calculateSugenoOutput(
                fuzzifiedInputs,
                ruleBase,
                andOperator,
                orOperator
        );

        return createSingleton(output);
    }

    @Override
    public String getType() {
        return "SUGENO";
    }

    @Override
    public Map<FuzzyRule, Double> getRuleStrengths(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        Map<FuzzyRule, Double> strengths = new LinkedHashMap<>();

        for (FuzzyRule rule : ruleBase.getActiveRules()) {
            if (rule.isSugenoRule()) {
                double strength = rule.evaluateAntecedent(fuzzifiedInputs, andOperator, orOperator);
                strength *= rule.getWeight();
                strengths.put(rule, strength);
            }
        }

        return strengths;
    }

    private double calculateSugenoOutput(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        double numerator = 0.0;
        double denominator = 0.0;

        for (FuzzyRule rule : ruleBase.getActiveRules()) {
            if (!rule.isSugenoRule()) continue;

            double strength = rule.evaluateAntecedent(fuzzifiedInputs, andOperator, orOperator);
            strength *= rule.getWeight();

            Double output = rule.getSugenoOutputValue();
            if (output == null) {
                throw new IllegalStateException("Sugeno rule missing output value");
            }

            numerator += strength * output;
            denominator += strength;
        }

        return (denominator == 0.0) ? 0.0 : numerator / denominator;
    }

    private FuzzySet createSingleton(double value) {
        MembershipFunction singletonMF = new MembershipFunction() {
            @Override
            public double getMembership(double x) {
                return (Math.abs(x - value) < 0.001) ? 1.0 : 0.0;
            }

            @Override
            public double[] getDefiningPoints() {
                return new double[]{outputMin, value, outputMax};
            }
        };

        return new FuzzySet("SugenoOutput", singletonMF);
    }
}