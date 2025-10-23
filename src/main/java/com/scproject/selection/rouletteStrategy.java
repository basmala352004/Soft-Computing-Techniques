package com.scproject.selection;

import com.scproject.chromosome.Chromosome;
import com.scproject.core.Population;

import java.util.*;

public class rouletteStrategy implements SelectionStrategy{
    @Override
    public Chromosome select(Population population) {

        List<Chromosome> chromosomes = new ArrayList<>(population.getIndividuals());
        int N = chromosomes.size();


        double totalFitness = 0.0;
        for (Chromosome c : chromosomes) {
            totalFitness += c.getFitness();
        }

        if (totalFitness == 0) {
            return chromosomes.get(new Random().nextInt(N));
        }

        double rand = Math.random() * totalFitness;
        double cumulative = 0.0;
        for (Chromosome c : chromosomes) {
            cumulative += c.getFitness();
            if (cumulative >= rand) {
                return c;
            }
        }

        return chromosomes.getLast();
    }

    @Override
    public Chromosome[] select(Population population, int count) {
        return SelectionStrategy.super.select(population, count);
    }
}
