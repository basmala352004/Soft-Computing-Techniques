package com.scproject.fuzzy.core;

import com.scproject.fuzzy.defuzzification.DefuzzificationMethod;
import com.scproject.fuzzy.inference.InferenceEngine;
import com.scproject.fuzzy.operator.FuzzyOperator;
import com.scproject.fuzzy.operator.MinOperator;
import com.scproject.fuzzy.operator.MaxOperator;
import com.scproject.fuzzy.rulebase.RuleBase;

import java.util.*;

public class FuzzySystem {

    private List<FuzzyVariable> inputs;
    private FuzzyVariable output;
    private RuleBase ruleBase;
    private InferenceEngine inferenceEngine;
    private FuzzyOperator andOperator;
    private FuzzyOperator orOperator;
    private DefuzzificationMethod defuzzifier;

    private Map<String, Map<String, Double>> lastFuzzifiedInputs;
    private FuzzySet lastAggregatedOutput;

    public FuzzySystem() {
        this.inputs = new ArrayList<>();
        this.andOperator = new MinOperator();
        this.orOperator = new MaxOperator();
    }

    public double evaluate(Map<String, Double> crispInputs) {


        if (inputs.isEmpty()) throw new IllegalStateException("No inputs defined");
        if (inferenceEngine == null) throw new IllegalStateException("No inference engine");
        if (ruleBase == null) throw new IllegalStateException("No rule base");
        if (defuzzifier == null) throw new IllegalStateException("No defuzzifier");


        lastFuzzifiedInputs = fuzzifyInputs(crispInputs);


        lastAggregatedOutput = inferenceEngine.inferAndAggregate(
                lastFuzzifiedInputs,
                ruleBase,
                output,
                andOperator,
                orOperator
        );


        double minRange = (output != null) ? output.getMinRange() : 0;
        double maxRange = (output != null) ? output.getMaxRange() : 100;

        return defuzzifier.defuzzify(lastAggregatedOutput, minRange, maxRange);
    }

    private Map<String, Map<String, Double>> fuzzifyInputs(Map<String, Double> crispInputs) {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();

        for (FuzzyVariable input : inputs) {
            String varName = input.getName();
            Double value = crispInputs.get(varName);

            if (value == null) {
                throw new IllegalArgumentException("Missing input: " + varName);
            }
            Map<String, Double> memberships = new LinkedHashMap<>();
            for (Map.Entry<String, FuzzySet> entry : input.getFuzzySets().entrySet()) {
                double membership = entry.getValue().getMembership(value);
                memberships.put(entry.getKey(), membership);
            }

            result.put(varName, memberships);
        }

        return result;
    }
    public void addInput(FuzzyVariable var) { inputs.add(var); }
    public void setOutput(FuzzyVariable var) { output = var; }
    public void setRuleBase(RuleBase rb) { ruleBase = rb; }
    public void setInferenceEngine(InferenceEngine ie) { inferenceEngine = ie; }
    public void setAndOperator(FuzzyOperator op) { andOperator = op; }
    public void setOrOperator(FuzzyOperator op) { orOperator = op; }
    public void setDefuzzifier(DefuzzificationMethod df) { defuzzifier = df; }
    public Map<String, Map<String, Double>> getFuzzifiedInputs() { return lastFuzzifiedInputs; }
    public FuzzySet getAggregatedOutput() { return lastAggregatedOutput; }
    public String getInferenceType() { return inferenceEngine.getType(); }

    public Map<FuzzyRule, Double> getRuleStrengths() {
        return inferenceEngine.getRuleStrengths(lastFuzzifiedInputs, ruleBase, andOperator, orOperator);
    }
}