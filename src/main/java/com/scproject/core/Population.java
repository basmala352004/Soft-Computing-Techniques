package com.scproject.core;

import com.scproject.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Population {
    private List<Chromosome> individuals;
    private int size;
    private boolean sorted;

    public Population(int size) {
        this.size = size;
        this.individuals = new ArrayList<>(size);
        this.sorted = false;
    }

    public Population(List<Chromosome> individuals) {
        this.individuals = new ArrayList<>(individuals);
        this.size = individuals.size();
        this.sorted = false;
    }

    public void initialize(Chromosome prototype, int chromosomeLength) {
        individuals.clear();
        for (int i = 0; i < size; i++) {
            Chromosome individual = prototype.createNew(chromosomeLength);
            individual.initialize();
            individuals.add(individual);
        }
        sorted = false;
    }

    public void addIndividual(Chromosome individual) {
        individuals.add(individual);
        size++;
        sorted = false;
    }

    public void removeIndividual(int index) {
        individuals.remove(index);
        size--;
        sorted = false;
    }

    public Chromosome getIndividual(int index) {
        return individuals.get(index);
    }

    public void setIndividual(int index, Chromosome individual) {
        individuals.set(index, individual);
        sorted = false;
    }

    public void sortByFitness() {
        individuals.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
        sorted = true;
    }

    public Chromosome getBestIndividual() {
        if (!sorted) {
            sortByFitness();
        }
        return individuals.get(0);
    }

    public Chromosome getWorstIndividual() {
        if (!sorted) {
            sortByFitness();
        }
        return individuals.get(size - 1);
    }

    public double getAverageFitness() {
        double sum = 0;
        for (Chromosome individual : individuals) {
            sum += individual.getFitness();
        }
        return sum / size;
    }

    public double getTotalFitness() {
        double sum = 0;
        for (Chromosome individual : individuals) {
            sum += individual.getFitness();
        }
        return sum;
    }

    public double getMaxFitness() {
        return getBestIndividual().getFitness();
    }

    public double getMinFitness() {
        return getWorstIndividual().getFitness();
    }

    public double getStandardDeviation() {
        double mean = getAverageFitness();
        double sumSquaredDiff = 0;

        for (Chromosome individual : individuals) {
            double diff = individual.getFitness() - mean;
            sumSquaredDiff += diff * diff;
        }

        return Math.sqrt(sumSquaredDiff / size);
    }

    public List<Chromosome> getTopN(int n) {
        if (!sorted) {
            sortByFitness();
        }
        return new ArrayList<>(individuals.subList(0, Math.min(n, size)));
    }

    public List<Chromosome> getIndividuals() {
        return new ArrayList<>(individuals);
    }

    public void setIndividuals(List<Chromosome> individuals) {
        this.individuals = new ArrayList<>(individuals);
        this.size = individuals.size();
        this.sorted = false;
    }

    public int getSize() {
        return size;
    }

    public void clear() {
        individuals.clear();
        size = 0;
        sorted = false;
    }

    public Population clone() {
        List<Chromosome> clonedIndividuals = new ArrayList<>();
        for (Chromosome individual : individuals) {
            clonedIndividuals.add(individual.clone());
        }
        return new Population(clonedIndividuals);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Population{size=").append(size);
        sb.append(", avgFitness=").append(String.format("%.4f", getAverageFitness()));
        sb.append(", maxFitness=").append(String.format("%.4f", getMaxFitness()));
        sb.append(", minFitness=").append(String.format("%.4f", getMinFitness()));
        sb.append("}");
        return sb.toString();
    }
}