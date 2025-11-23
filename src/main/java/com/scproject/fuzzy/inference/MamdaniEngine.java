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

/**
 * Mamdani Fuzzy Inference Engine
 *
 * Implements the complete Mamdani inference process:
 * 1. Fuzzification (done externally)
 * 2. Rule Evaluation - calculate firing strength for each rule
 * 3. Implication - clip each consequent at its rule's firing strength
 * 4. Aggregation - combine all clipped consequents using MAX operation
 * 5. Defuzzification (done externally with the aggregated set)
 */
public class MamdaniEngine implements InferenceEngine {

    private AggregationMethod aggregationMethod;

    public MamdaniEngine() {
        this.aggregationMethod = new MaxAggregation();
    }

    public MamdaniEngine(AggregationMethod aggregationMethod) {
        this.aggregationMethod = aggregationMethod;
    }

    /**
     * Performs Mamdani inference: evaluates rules, applies implication, and aggregates.
     *
     * @param fuzzifiedInputs Map of variable name → (fuzzy set name → membership degree)
     * @param ruleBase Collection of fuzzy rules
     * @param outputVariable The output linguistic variable
     * @param andOperator Operator for AND (typically MIN)
     * @param orOperator Operator for OR (typically MAX)
     * @return Aggregated fuzzy set ready for defuzzification
     */
    @Override
    public FuzzySet inferAndAggregate(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyVariable outputVariable,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        // Step 1: Evaluate all rules to get firing strengths
        Map<FuzzyRule, Double> ruleStrengths = evaluateRules(
                fuzzifiedInputs,
                ruleBase,
                andOperator,
                orOperator
        );

        // Step 2: Apply implication - clip each consequent at its firing strength
        List<FuzzySet> clippedConsequents = applyImplication(
                ruleStrengths,
                outputVariable
        );

        // Step 3: Aggregate all clipped consequents using MAX operation
        return aggregationMethod.aggregate(clippedConsequents);
    }

    /**
     * Evaluates all active rules to calculate their firing strengths.
     */
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

            // Apply rule weight
            strength *= rule.getWeight();

            ruleStrengths.put(rule, strength);
        }

        return ruleStrengths;
    }

    /**
     * Applies implication by clipping each consequent at its rule's firing strength.
     * This implements the "alpha-cut" or "clipping" method from the lectures.
     */
    private List<FuzzySet> applyImplication(
            Map<FuzzyRule, Double> ruleStrengths,
            FuzzyVariable outputVariable) {

        List<FuzzySet> clippedSets = new ArrayList<>();

        for (Map.Entry<FuzzyRule, Double> entry : ruleStrengths.entrySet()) {
            FuzzyRule rule = entry.getKey();
            double firingStrength = entry.getValue();

            // Get the consequent fuzzy set from the output variable
            String consequentName = rule.getConsequentFuzzySet();
            FuzzySet originalConsequent = outputVariable.getFuzzySets().get(consequentName);

            if (originalConsequent == null) {
                throw new IllegalStateException(
                        "Consequent fuzzy set not found: " + consequentName
                );
            }

            // Clip the consequent at the firing strength (alpha-cut)
            FuzzySet clipped = clipAtAlpha(originalConsequent, firingStrength);
            clippedSets.add(clipped);
        }

        return clippedSets;
    }

    /**
     * Clips a fuzzy set at the given alpha level.
     * Returns a new fuzzy set where membership is min(original_membership, alpha).
     */
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

    /**
     * Gets the rule firing strengths for debugging/visualization.
     */
    public Map<FuzzyRule, Double> getRuleStrengths(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator) {

        return evaluateRules(fuzzifiedInputs, ruleBase, andOperator, orOperator);
    }
}