package com.scproject.fuzzy.defuzzification;

import com.scproject.fuzzy.core.FuzzySet;

public interface DefuzzificationMethod {

    double defuzzify(FuzzySet aggregatedSet, double minRange, double maxRange);
}
