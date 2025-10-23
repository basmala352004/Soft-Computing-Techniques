package com.scproject.mutation;


import com.scproject.chromosome.Chromosome;

/**
 * Interface for different mutation strategies.
 * Mutation introduces random changes to maintain genetic diversity.
 */
public interface MutationStrategy {

    /**
     * Mutate the given chromosome in-place.
     * The mutation should modify the chromosome's genes according to the strategy.
     *
     * @param chromosome The chromosome to mutate
     */
    Chromosome mutate(Chromosome chromosome);

    /**
     * Set the mutation rate (probability of mutating each gene).
     * Default implementation does nothing; strategies can override if needed.
     *
     * @param mutationRate The mutation rate (0.0 to 1.0)
     */
    default void setMutationRate(double mutationRate) {
        // Default: no-op, strategies can override
    }

    /**
     * Check if this mutation strategy is applicable to the given chromosome type.
     * Default implementation returns true (applicable to all types).
     *
     * @param chromosome The chromosome to check
     * @return true if applicable, false otherwise
     */
    default boolean isApplicable(Chromosome chromosome) {
        return true;
    }
}
