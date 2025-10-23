package com.scproject.fitness;

import com.scproject.chromosome.Chromosome;
import com.scproject.chromosome.IntegerChromosome;

import java.util.Map;
import java.util.Set;


public class RoutingFitnessFunction implements FitnessFunction {

    private final Map<Integer, Set<Integer>> towerConnections;


    public RoutingFitnessFunction(Map<Integer, Set<Integer>> towerConnections) {
        this.towerConnections = towerConnections;
    }


    @Override
    public double evaluate(Chromosome chromosome) {
        if (!(chromosome instanceof IntegerChromosome)) {
            throw new IllegalArgumentException("RoutingFitnessFunction only supports IntegerChromosome");
        }

        Integer[] route = (Integer[]) chromosome.getGenes();
        double score = 0.0;

        for (int i = 0; i < route.length - 1; i++) {
            int from = route[i];
            int to = route[i + 1];


            if (towerConnections.getOrDefault(from, Set.of()).contains(to)) {
                score += 1.0;
            }
        }

        return score;
    }
}