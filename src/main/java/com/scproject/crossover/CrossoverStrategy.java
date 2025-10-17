package com.scproject.crossover;

import com.scproject.chromosome.Chromosome;

/**
 * Interface for different crossover (recombination) strategies.
 * Crossover combines genetic material from two parents to create offspring.
 */
public interface CrossoverStrategy {

    /**
     * Perform crossover between two parent chromosomes to produce offspring.
     *
     * @param parent1 The first parent chromosome
     * @param parent2 The second parent chromosome
     * @return Array containing two offspring chromosomes
     */
    Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate);

    /**
     * Check if this crossover strategy is applicable to the given chromosome type.
     * Default implementation returns true (applicable to all types).
     *
     * @param chromosome The chromosome to check
     * @return true if applicable, false otherwise
     */
    default boolean isApplicable(Chromosome chromosome) {
        return true;
    }
    String getName();
}
