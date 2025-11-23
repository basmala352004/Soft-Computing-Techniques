package com.scproject.ga.selection;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.core.Population;

import java.util.*;

public class rankSelection implements SelectionStrategy{
    @Override
    public Chromosome select(Population population) {
        List<Chromosome> chromosomes = new ArrayList<>(population.getIndividuals());

        int N = chromosomes.size();

        chromosomes.sort(Comparator.comparingDouble(Chromosome::getFitness));

        double[] rankFitness = new double[N];
        for (int i = 0; i < N; i++) {
            rankFitness[i] = i + 1;
        }

        double totalRankFitness = Arrays.stream(rankFitness).sum();

        double rand = Math.random() * totalRankFitness;
        double cumulative = 0.0;

        for (int i = 0; i < N; i++) {
            cumulative += rankFitness[i];
            if (cumulative >= rand) {
                return chromosomes.get(i);
            }
        }

        return chromosomes.getLast();
    }

    @Override
    public Chromosome[] select(Population population, int count) {
        return SelectionStrategy.super.select(population, count);
    }
}
