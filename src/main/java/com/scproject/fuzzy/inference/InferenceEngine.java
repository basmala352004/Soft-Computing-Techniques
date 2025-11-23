package com.scproject.fuzzy.inference;

import com.scproject.fuzzy.core.FuzzySet;
import com.scproject.fuzzy.core.FuzzyVariable;
import com.scproject.fuzzy.operator.FuzzyOperator;
import com.scproject.fuzzy.rulebase.RuleBase;

import java.util.Map;

public interface InferenceEngine {

    FuzzySet inferAndAggregate(
            Map<String, Map<String, Double>> fuzzifiedInputs,
            RuleBase ruleBase,
            FuzzyVariable outputVariable,
            FuzzyOperator andOperator,
            FuzzyOperator orOperator);
}
