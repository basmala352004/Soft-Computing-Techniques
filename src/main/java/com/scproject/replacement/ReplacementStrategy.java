package com.scproject.replacement;


import com.scproject.chromosome.Chromosome;
import com.scproject.core.Population;

import java.util.List;

/**
 * Interface for different replacement strategies.
 * Replacement determines how offspring are integrated into the population.
 */
public interface ReplacementStrategy {

    /**
     * Replace individuals in the current population with offspring.
     *
     * @param currentPopulation The current population
     * @param offspring The offspring to integrate
     * @return The new population after replacement
     */
    Population replace(Population currentPopulation, List<Chromosome> offspring);

    /**
     * Set the number of elite individuals to preserve (if applicable).
     * Default implementation does nothing; strategies can override if needed.
     *
     * @param eliteCount Number of best individuals to preserve
     */
    default void setEliteCount(int eliteCount) {
        // Default: no-op, strategies can override
    }
}