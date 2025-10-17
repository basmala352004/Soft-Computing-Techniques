package com.scproject.core;


import com.scproject.chromosome.Chromosome;
import com.scproject.selection.SelectionStrategy;
import com.scproject.crossover.CrossoverStrategy;
import com.scproject.mutation.MutationStrategy;
import com.scproject.replacement.ReplacementStrategy;
import com.scproject.fitness.FitnessFunction;
import com.scproject.constraint.ConstraintHandler;
import com.scproject.core.GAConfiguration;
import com.scproject.core.Population;


import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
    private GAConfiguration config;
    private Population population;
    private FitnessFunction fitnessFunction;
    private ConstraintHandler constraintHandler;
    private Chromosome bestSolution;
    private double bestFitness;
    private List<Double> fitnessHistory;

    public GeneticAlgorithm() {
        this.config = new GAConfiguration();
        this.fitnessHistory = new ArrayList<>();
        this.bestFitness = Double.NEGATIVE_INFINITY;
    }

    public GeneticAlgorithm(GAConfiguration config) {
        this.config = config;
        this.fitnessHistory = new ArrayList<>();
        this.bestFitness = Double.NEGATIVE_INFINITY;
    }

    public void run() {
        validateConfiguration();
        initialize();

        for (int generation = 0; generation < config.getGenerations(); generation++) {
            evaluatePopulation();

            List<Chromosome> parents = selectParents();
            List<Chromosome> offspring = performCrossover(parents);
            performMutation(offspring);

            evaluateOffspring(offspring);
            population = replacePopulation(offspring);

            updateBestSolution(generation);

            if (config.isVerbose() && generation % config.getPrintFrequency() == 0) {
                printGenerationStats(generation);
            }
        }

        evaluatePopulation();
        updateBestSolution(config.getGenerations());

        if (config.isVerbose()) {
            printFinalResults();
        }
    }

    private void initialize() {
        population = new Population(config.getPopulationSize());
        population.initialize(config.getChromosomePrototype(), config.getChromosomeLength());
    }

    private void evaluatePopulation() {
        for (Chromosome individual : population.getIndividuals()) {
            if (!individual.isEvaluated()) {
                evaluateIndividual(individual);
            }
        }
    }

    private void evaluateIndividual(Chromosome individual) {
        double fitness = fitnessFunction.evaluate(individual);

        if (constraintHandler != null && !constraintHandler.isFeasible(individual)) {
            fitness = constraintHandler.adjustFitness(individual, fitness);
        }

        individual.setFitness(fitness);
        individual.setEvaluated(true);
    }

    private List<Chromosome> selectParents() {
        SelectionStrategy selectionStrategy = config.getSelectionStrategy();
        List<Chromosome> parents = new ArrayList<>();

        int numParents = config.getNumberOfParents();
        if (numParents % 2 != 0) {
            numParents++;
        }

        for (int i = 0; i < numParents; i++) {
            Chromosome parent = selectionStrategy.select(population);
            parents.add(parent);
        }

        return parents;
    }

    private List<Chromosome> performCrossover(List<Chromosome> parents) {
        CrossoverStrategy crossoverStrategy = config.getCrossoverStrategy();
        List<Chromosome> offspring = new ArrayList<>();

        for (int i = 0; i < parents.size() - 1; i += 2) {
            if (Math.random() < config.getCrossoverRate()) {
                Chromosome[] children = crossoverStrategy.crossover(parents.get(i), parents.get(i + 1));
                offspring.add(children[0]);
                offspring.add(children[1]);
            } else {
                offspring.add(parents.get(i).clone());
                offspring.add(parents.get(i + 1).clone());
            }
        }

        return offspring;
    }

    private void performMutation(List<Chromosome> offspring) {
        MutationStrategy mutationStrategy = config.getMutationStrategy();

        for (Chromosome individual : offspring) {
            if (Math.random() < config.getMutationRate()) {
                mutationStrategy.mutate(individual);
            }
        }
    }

    private void evaluateOffspring(List<Chromosome> offspring) {
        for (Chromosome individual : offspring) {
            evaluateIndividual(individual);
        }
    }

    private Population replacePopulation(List<Chromosome> offspring) {
        ReplacementStrategy replacementStrategy = config.getReplacementStrategy();
        return replacementStrategy.replace(population, offspring);
    }

    private void updateBestSolution(int generation) {
        Chromosome currentBest = population.getBestIndividual();

        if (currentBest.getFitness() > bestFitness) {
            bestFitness = currentBest.getFitness();
            bestSolution = currentBest.clone();
        }

        fitnessHistory.add(bestFitness);
    }

    private void printGenerationStats(int generation) {
        double avgFitness = population.getAverageFitness();
        double maxFitness = population.getBestIndividual().getFitness();
        double minFitness = population.getWorstIndividual().getFitness();

        System.out.printf("Generation %d | Best: %.4f | Avg: %.4f | Min: %.4f%n",
                generation, maxFitness, avgFitness, minFitness);
    }

    private void printFinalResults() {
        System.out.println("\n========== Final Results ==========");
        System.out.printf("Best Fitness: %.6f%n", bestFitness);
        System.out.println("Best Solution: " + bestSolution);
        System.out.println("===================================\n");
    }

    private void validateConfiguration() {
        if (fitnessFunction == null) {
            throw new IllegalStateException("Fitness function must be set before running GA");
        }
        if (config.getChromosomePrototype() == null) {
            throw new IllegalStateException("Chromosome prototype must be set before running GA");
        }
        if (config.getPopulationSize() <= 0) {
            throw new IllegalStateException("Population size must be positive");
        }
        if (config.getGenerations() <= 0) {
            throw new IllegalStateException("Number of generations must be positive");
        }
    }

    // Getters and Setters
    public Chromosome getBestSolution() {
        return bestSolution;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public List<Double> getFitnessHistory() {
        return new ArrayList<>(fitnessHistory);
    }

    public Population getPopulation() {
        return population;
    }

    // Configuration setters for user convenience
    public void setPopulationSize(int size) {
        config.setPopulationSize(size);
    }

    public void setGenerations(int generations) {
        config.setGenerations(generations);
    }

    public void setChromosomeLength(int length) {
        config.setChromosomeLength(length);
    }

    public void setNumberOfParents(int numberOfParents) {
        config.setNumberOfParents(numberOfParents);
    }

    public void setCrossoverRate(double rate) {
        config.setCrossoverRate(rate);
    }

    public void setMutationRate(double rate) {
        config.setMutationRate(rate);
    }

    public void setFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    public void setConstraintHandler(ConstraintHandler constraintHandler) {
        this.constraintHandler = constraintHandler;
    }

    public void setSelectionStrategy(SelectionStrategy strategy) {
        config.setSelectionStrategy(strategy);
    }

    public void setCrossoverStrategy(CrossoverStrategy strategy) {
        config.setCrossoverStrategy(strategy);
    }

    public void setMutationStrategy(MutationStrategy strategy) {
        config.setMutationStrategy(strategy);
    }

    public void setReplacementStrategy(ReplacementStrategy strategy) {
        config.setReplacementStrategy(strategy);
    }

    public void setChromosomePrototype(Chromosome prototype) {
        config.setChromosomePrototype(prototype);
    }

    public void setVerbose(boolean verbose) {
        config.setVerbose(verbose);
    }

    public void setPrintFrequency(int frequency) {
        config.setPrintFrequency(frequency);
    }

    public GAConfiguration getConfig() {
        return config;
    }

    public void setConfig(GAConfiguration config) {
        this.config = config;
    }
}
