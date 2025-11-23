package com.scproject.ga.replacement;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.core.Population;

import java.util.*;


public class GenerationalReplacement implements ReplacementStrategy {


    @Override
    public Population replace(Population currentPopulation, List<Chromosome> offspring) {
        int popSize = currentPopulation.getSize();

        if (offspring.size() < popSize) {
            throw new IllegalArgumentException(
                    "Offspring count (" + offspring.size() +
                            ") is less than population size (" + popSize + ")"
            );
        }

        offspring.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());

        List<Chromosome> newIndividuals = offspring.subList(0, Math.min(popSize, offspring.size()));


        return new Population(newIndividuals);
    }
}
