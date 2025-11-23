package com.scproject.ga.chromosome;


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


    public abstract void initialize();


    public abstract Object getGene(int index);


    public abstract void setGene(int index, Object value);


    public abstract Object[] getGenes();


    public abstract void setGenes(Object[] genes);


    public abstract Chromosome createNew(int length);


    @Override
    public abstract Chromosome clone();


    @Override
    public abstract String toString();

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


    public void resetEvaluation() {
        this.evaluated = false;
    }
}