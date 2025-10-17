package com.scproject.fitness;

import com.scproject.chromosome.Chromosome;

/**
 * Interface for defining problem-specific fitness evaluation functions.
 * Each problem domain must implement this interface to evaluate chromosome quality.
 */
@FunctionalInterface
public interface FitnessFunction {

    /**
     * Evaluate the fitness of a given chromosome.
     * Higher fitness values indicate better solutions.
     *
     * @param chromosome The chromosome to evaluate
     * @return The fitness value (higher is better)
     */
    double evaluate(Chromosome chromosome);
}
