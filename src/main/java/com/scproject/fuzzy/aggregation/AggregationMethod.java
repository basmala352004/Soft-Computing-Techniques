package com.scproject.fuzzy.aggregation;

import com.scproject.fuzzy.core.FuzzySet;

import java.util.Collection;

public interface AggregationMethod {
    FuzzySet aggregate(Collection<FuzzySet> clippedSets);
}
