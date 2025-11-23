package com.scproject.ga.core;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.crossover.CrossoverStrategy;
import com.scproject.ga.crossover.UniformMethod;
import com.scproject.ga.mutation.MutationStrategy;
import com.scproject.ga.replacement.ElitistReplacement;
import com.scproject.ga.replacement.ReplacementStrategy;
import com.scproject.ga.selection.SelectionStrategy;
import com.scproject.ga.selection.rankSelection;
import  com.scproject.selection.*;
import  com.scproject.crossover.*;
import  com.scproject.mutation.*;
import  com.scproject.replacement.*;


public class GAConfiguration {
    private int populationSize;
    private int generations;
    private int chromosomeLength;
    private int numberOfParents;
    private double crossoverRate;
    private double mutationRate;
    private boolean verbose;
    private int printFrequency;

    private Chromosome chromosomePrototype;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;
    private ReplacementStrategy replacementStrategy;

    public GAConfiguration() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        this.populationSize = 100;
        this.generations = 100;
        this.chromosomeLength = 10;
        this.numberOfParents = 100;
        this.crossoverRate = 0.8;
        this.mutationRate = 0.01;
        this.verbose = true;
        this.printFrequency = 10;

        this.selectionStrategy = new rankSelection();
        this.crossoverStrategy = new UniformMethod();
        this.mutationStrategy = null;
        this.replacementStrategy = new ElitistReplacement();
        this.replacementStrategy.setEliteCount(2);
    }

    public void validate() {
        if (populationSize <= 0) {
            throw new IllegalArgumentException("Population size must be positive");
        }
        if (generations <= 0) {
            throw new IllegalArgumentException("Number of generations must be positive");
        }
        if (chromosomeLength <= 0) {
            throw new IllegalArgumentException("Chromosome length must be positive");
        }
        if (numberOfParents <= 0 || numberOfParents > populationSize) {
            throw new IllegalArgumentException("Number of parents must be between 1 and population size");
        }
        if (crossoverRate < 0 || crossoverRate > 1) {
            throw new IllegalArgumentException("Crossover rate must be between 0 and 1");
        }
        if (mutationRate < 0 || mutationRate > 1) {
            throw new IllegalArgumentException("Mutation rate must be between 0 and 1");
        }
        if (printFrequency <= 0) {
            throw new IllegalArgumentException("Print frequency must be positive");
        }
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getGenerations() {
        return generations;
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public int getNumberOfParents() {
        return numberOfParents;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public int getPrintFrequency() {
        return printFrequency;
    }

    public Chromosome getChromosomePrototype() {
        return chromosomePrototype;
    }

    public SelectionStrategy getSelectionStrategy() {
        return selectionStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    public ReplacementStrategy getReplacementStrategy() {
        return replacementStrategy;
    }

    public void setPopulationSize(int populationSize) {
        if (populationSize <= 0) {
            throw new IllegalArgumentException("Population size must be positive");
        }
        this.populationSize = populationSize;
    }

    public void setGenerations(int generations) {
        if (generations <= 0) {
            throw new IllegalArgumentException("Number of generations must be positive");
        }
        this.generations = generations;
    }

    public void setChromosomeLength(int chromosomeLength) {
        if (chromosomeLength <= 0) {
            throw new IllegalArgumentException("Chromosome length must be positive");
        }
        this.chromosomeLength = chromosomeLength;
    }

    public void setNumberOfParents(int numberOfParents) {
        if (numberOfParents <= 0) {
            throw new IllegalArgumentException("Number of parents must be positive");
        }
        this.numberOfParents = numberOfParents;
    }

    public void setCrossoverRate(double crossoverRate) {
        if (crossoverRate < 0 || crossoverRate > 1) {
            throw new IllegalArgumentException("Crossover rate must be between 0 and 1");
        }
        this.crossoverRate = crossoverRate;
    }

    public void setMutationRate(double mutationRate) {
        if (mutationRate < 0 || mutationRate > 1) {
            throw new IllegalArgumentException("Mutation rate must be between 0 and 1");
        }
        this.mutationRate = mutationRate;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setPrintFrequency(int printFrequency) {
        if (printFrequency <= 0) {
            throw new IllegalArgumentException("Print frequency must be positive");
        }
        this.printFrequency = printFrequency;
    }

    public void setChromosomePrototype(Chromosome chromosomePrototype) {
        this.chromosomePrototype = chromosomePrototype;
    }

    public void setSelectionStrategy(SelectionStrategy selectionStrategy) {
        if (selectionStrategy == null) {
            throw new IllegalArgumentException("Selection strategy cannot be null");
        }
        this.selectionStrategy = selectionStrategy;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        if (crossoverStrategy == null) {
            throw new IllegalArgumentException("Crossover strategy cannot be null");
        }
        this.crossoverStrategy = crossoverStrategy;
    }

    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        if (mutationStrategy == null) {
            throw new IllegalArgumentException("Mutation strategy cannot be null");
        }
        this.mutationStrategy = mutationStrategy;
    }

    public void setReplacementStrategy(ReplacementStrategy replacementStrategy) {
        if (replacementStrategy == null) {
            throw new IllegalArgumentException("Replacement strategy cannot be null");
        }
        this.replacementStrategy = replacementStrategy;
    }

    @Override
    public String toString() {
        return "GAConfiguration{" +
                "populationSize=" + populationSize +
                ", generations=" + generations +
                ", chromosomeLength=" + chromosomeLength +
                ", numberOfParents=" + numberOfParents +
                ", crossoverRate=" + crossoverRate +
                ", mutationRate=" + mutationRate +
                ", selectionStrategy=" + selectionStrategy.getClass().getSimpleName() +
                ", crossoverStrategy=" + crossoverStrategy.getClass().getSimpleName() +
                ", mutationStrategy=" + (mutationStrategy != null ? mutationStrategy.getClass().getSimpleName() : "null") +
                ", replacementStrategy=" + replacementStrategy.getClass().getSimpleName() +
                '}';
    }

    public GAConfiguration copy() {
        GAConfiguration copy = new GAConfiguration();
        copy.populationSize = this.populationSize;
        copy.generations = this.generations;
        copy.chromosomeLength = this.chromosomeLength;
        copy.numberOfParents = this.numberOfParents;
        copy.crossoverRate = this.crossoverRate;
        copy.mutationRate = this.mutationRate;
        copy.verbose = this.verbose;
        copy.printFrequency = this.printFrequency;
        copy.chromosomePrototype = this.chromosomePrototype;
        copy.selectionStrategy = this.selectionStrategy;
        copy.crossoverStrategy = this.crossoverStrategy;
        copy.mutationStrategy = this.mutationStrategy;
        copy.replacementStrategy = this.replacementStrategy;
        return copy;
    }
}