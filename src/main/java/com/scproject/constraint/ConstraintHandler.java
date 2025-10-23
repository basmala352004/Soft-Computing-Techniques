package com.scproject.constraint;


import com.scproject.chromosome.Chromosome;

/**
 * Interface for handling constraint violations and infeasible solutions.
 * Provides methods to check feasibility and adjust fitness accordingly.
 */
public interface ConstraintHandler {

    /**
     * Check if a chromosome represents a feasible solution.
     *
     * @param chromosome The chromosome to check
     * @return true if feasible, false if it violates constraints
     */
    boolean isFeasible(Chromosome chromosome);

    /**
     * Adjust the fitness value for infeasible solutions.
     * This method is called when a chromosome is infeasible.
     *
     * Common strategies:
     * - Penalty method: reduce fitness proportional to constraint violation
     * - Death penalty: return very low fitness (e.g., Double.NEGATIVE_INFINITY)
     * - Repair: attempt to fix the solution (modify chromosome in-place)
     *
     * @param chromosome The infeasible chromosome
     * @param originalFitness The original fitness before adjustment
     * @return The adjusted fitness value
     */
    double adjustFitness(Chromosome chromosome, double originalFitness);

    /**
     * Calculate the degree of constraint violation.
     * Default implementation returns 0.0 (no violation measurement).
     *
     * @param chromosome The chromosome to evaluate
     * @return A measure of constraint violation (0.0 = no violation)
     */
    default double getViolationMeasure(Chromosome chromosome) {
        return 0.0;
    }

    /**
     * Attempt to repair an infeasible solution.
     * Default implementation does nothing (no repair).
     *
     * @param chromosome The chromosome to repair
     * @return true if repair was successful, false otherwise
     */
    Chromosome repair(Chromosome chromosome);
}
