package com.scproject.fitness;

import com.scproject.chromosome.Chromosome;
import com.scproject.chromosome.IntegerChromosome;

import java.util.Map;
import java.util.Set;

public class RoutingFitnessFunction implements FitnessFunction {

    private final Map<Integer, Set<Integer>> towerConnections;
    private final Map<Integer, Double> towerLoad;

    public RoutingFitnessFunction(Map<Integer, Set<Integer>> towerConnections, Map<Integer, Double> towerLoad) {
        this.towerConnections = towerConnections;
        this.towerLoad = towerLoad;
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
                double loadPenalty = towerLoad.getOrDefault(to, 0.0);
                score += (1.0 - loadPenalty); // Prefer low-load towers
            }
        }

        // Bonus for shorter routes
        score += (5.0 - route.length); // shorter = better

        return score;
    }
}