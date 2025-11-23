package com.scproject.ga.replacement;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.core.Population;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ElitistReplacement implements ReplacementStrategy {

    private int eliteCount = 1;
    private int replacementCount = 2;


    public void setReplacementCount(int count) {
        this.replacementCount = Math.max(1, count);
    }

    @Override
    public void setEliteCount(int eliteCount) {
        this.eliteCount = Math.max(1, eliteCount);
    }

    @Override
    public Population replace(Population currentPopulation, List<Chromosome> offspring) {
        List<Chromosome> individuals = new ArrayList<>(currentPopulation.getIndividuals());
        int popSize = individuals.size();

        individuals.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        List<Chromosome> elites = new ArrayList<>();
        for (int i = 0; i < eliteCount && i < individuals.size(); i++) {
            elites.add(individuals.get(i).clone());
        }

        List<Chromosome> others = new ArrayList<>(individuals.subList(eliteCount, individuals.size()));

        int replaceCount = Math.min(replacementCount, Math.min(offspring.size(), others.size()));
        for (int i = 0; i < replaceCount; i++) {
            others.set(others.size() - 1 - i, offspring.get(i));
        }

        List<Chromosome> newPopulation = new ArrayList<>();
        newPopulation.addAll(elites);
        newPopulation.addAll(others);

        while (newPopulation.size() > popSize) {
            newPopulation.remove(newPopulation.size() - 1);
        }

        return new Population(newPopulation);
    }
}
