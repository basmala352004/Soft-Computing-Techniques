package com.scproject.replacement;

import com.scproject.chromosome.Chromosome;
import com.scproject.core.Population;

import java.util.ArrayList;
import java.util.List;


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

        List<Chromosome> newIndividuals = new ArrayList<>();

        for (Chromosome chromosome : offspring) {
            newIndividuals.add(chromosome);
        }

        return new Population(newIndividuals);
    }
}
