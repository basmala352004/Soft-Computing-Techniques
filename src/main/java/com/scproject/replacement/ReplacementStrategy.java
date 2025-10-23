package com.scproject.replacement;


import com.scproject.chromosome.Chromosome;
import com.scproject.core.Population;

import java.util.List;


public interface ReplacementStrategy {

    Population replace(Population currentPopulation, List<Chromosome> offspring);

    default void setEliteCount(int eliteCount) {
        //default, strategies can override
    }
}