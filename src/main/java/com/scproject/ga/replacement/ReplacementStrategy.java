package com.scproject.ga.replacement;


import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.core.Population;

import java.util.List;


public interface ReplacementStrategy {

    Population replace(Population currentPopulation, List<Chromosome> offspring);

    default void setEliteCount(int eliteCount) {
        //default, strategies can override
    }
}