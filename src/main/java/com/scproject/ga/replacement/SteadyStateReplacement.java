package com.scproject.ga.replacement;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.core.Population;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SteadyStateReplacement implements ReplacementStrategy {

    private int replacementCount = 2;

    public void setReplacementCount(int count) {
        this.replacementCount = Math.max(1, count);
    }

    @Override
    public Population replace(Population currentPopulation, List<Chromosome> offspring) {
        List<Chromosome> individuals = new ArrayList<>(currentPopulation.getIndividuals());
        int popSize = individuals.size();

        individuals.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        int replaceCount = Math.min(replacementCount, Math.min(offspring.size(), popSize));
        for (int i = 0; i < replaceCount; i++) {
            individuals.set(popSize - 1 - i, offspring.get(i));
        }

        return new Population(individuals);
    }
}
