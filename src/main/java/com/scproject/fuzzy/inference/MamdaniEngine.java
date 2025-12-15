package com.scproject.fuzzy.inference;

import com.scproject.fuzzy.aggregation.AggregationMethod;
import com.scproject.fuzzy.aggregation.MaxAggregation;
import com.scproject.fuzzy.core.FuzzySet;
import com.scproject.fuzzy.core.FuzzyVariable;
import com.scproject.fuzzy.core.FuzzyRule;
import com.scproject.fuzzy.membership.MembershipFunction;
import com.scproject.fuzzy.operator.FuzzyOperator;
import com.scproject.fuzzy.rulebase.RuleBase;

import java.util.*;


public class MamdaniEngine implements InferenceEngine {

    private AggregationMethod aggregationMethod;

    public MamdaniEngine() {
        this.aggregationMethod = new MaxAggregation();
    }

    public MamdaniEngine(AggregationMethod aggregationMethod) {
        this.aggregationMethod = aggregationMethod;
    }


    @Override
    public FuzzySet inferAndAggregate(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyVariable outputVariable,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {


        Map<FuzzyRule, Double> ruleStrengths = evaluateRules(
                fuzzifiedInputs,
                ruleBase,
                andOperator,
                orOperator
        );


        List<FuzzySet> clippedConsequents = applyImplication(
                ruleStrengths,
                outputVariable
        );


        return aggregationMethod.aggregate(clippedConsequents);
    }


    private Map<FuzzyRule, Double> evaluateRules(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        Map<FuzzyRule, Double> ruleStrengths = new LinkedHashMap<>();

        for (FuzzyRule rule : ruleBase.getActiveRules()) {
            double strength = rule.evaluateAntecedent(
                    fuzzifiedInputs,
                    andOperator,
                    orOperator
            );


            strength *= rule.getWeight();

            ruleStrengths.put(rule, strength);
        }

        return ruleStrengths;
    }


    private List<FuzzySet> applyImplication(
            Map<FuzzyRule, Double> ruleStrengths,
            FuzzyVariable outputVariable) {

        List<FuzzySet> clippedSets = new ArrayList<>();

        for (Map.Entry<FuzzyRule, Double> entry : ruleStrengths.entrySet()) {
            FuzzyRule rule = entry.getKey();
            double firingStrength = entry.getValue();


            String consequentName = rule.getConsequentFuzzySet();
            FuzzySet originalConsequent = outputVariable.getFuzzySets().get(consequentName);

            if (originalConsequent == null) {
                throw new IllegalStateException(
                        "Consequent fuzzy set not found: " + consequentName
                );
            }


            FuzzySet clipped = clipAtAlpha(originalConsequent, firingStrength);
            clippedSets.add(clipped);
        }

        return clippedSets;
    }


    private FuzzySet clipAtAlpha(FuzzySet originalSet, double alpha) {
        MembershipFunction originalMF = originalSet.getMembershipFunction();

        MembershipFunction clippedMF = new MembershipFunction() {
            @Override
            public double getMembership(double x) {
                double originalMembership = originalMF.getMembership(x);
                return Math.min(originalMembership, alpha);
            }

            @Override
            public double[] getDefiningPoints() {
                return originalMF.getDefiningPoints();
            }
        };

        return new FuzzySet(originalSet.getName() + "_α" + alpha, clippedMF);
    }

    public Map<FuzzyRule, Double> getRuleStrengths(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        return evaluateRules(fuzzifiedInputs, ruleBase, andOperator, orOperator);
    }
    @Override
    public String getType() {
        return "MAMDANI";
    }
}