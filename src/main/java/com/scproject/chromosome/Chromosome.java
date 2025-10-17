package com.scproject.chromosome;


/**
 * Abstract base class for all chromosome types in the genetic algorithm.
 * Provides common functionality and defines the contract for chromosome operations.
 */
public abstract class Chromosome implements Cloneable {
    protected double fitness;
    protected boolean evaluated;
    protected int length;

    public Chromosome(int length) {
        this.length = length;
        this.fitness = 0.0;
        this.evaluated = false;
    }

    /**
     * Initialize the chromosome with random values.
     */
    public abstract void initialize();

    /**
     * Get the gene at the specified index.
     * @param index The position of the gene
     * @return The gene value as an Object
     */
    public abstract Object getGene(int index);

    /**
     * Set the gene at the specified index.
     * @param index The position of the gene
     * @param value The new gene value
     */
    public abstract void setGene(int index, Object value);

    /**
     * Get all genes as an array.
     * @return Array containing all genes
     */
    public abstract Object[] getGenes();

    /**
     * Set all genes from an array.
     * @param genes Array containing new gene values
     */
    public abstract void setGenes(Object[] genes);

    /**
     * Create a new chromosome of the same type with the specified length.
     * @param length The length of the new chromosome
     * @return A new chromosome instance
     */
    public abstract Chromosome createNew(int length);

    /**
     * Create a deep copy of this chromosome.
     * @return A cloned chromosome
     */
    @Override
    public abstract Chromosome clone();

    /**
     * Get a string representation of the chromosome.
     * @return String representation
     */
    @Override
    public abstract String toString();

    // Common getters and setters
    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Reset evaluation status (useful after mutation/crossover).
     */
    public void resetEvaluation() {
        this.evaluated = false;
    }
}