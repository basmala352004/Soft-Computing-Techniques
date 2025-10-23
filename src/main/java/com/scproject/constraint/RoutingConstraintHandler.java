package com.scproject.constraint;

import com.scproject.chromosome.Chromosome;
import com.scproject.chromosome.IntegerChromosome;

import java.util.*;

public class RoutingConstraintHandler implements ConstraintHandler {

    private final Set<Integer> validTowerIds;
    private final Map<Integer, Set<Integer>> towerConnections;

    public RoutingConstraintHandler(Set<Integer> validTowerIds, Map<Integer, Set<Integer>> towerConnections) {
        this.validTowerIds = validTowerIds;
        this.towerConnections = towerConnections;
    }

    @Override
    public boolean isFeasible(Chromosome chromosome) {
        if (!(chromosome instanceof IntegerChromosome)) return false;

        Integer[] route = (Integer[]) chromosome.getGenes();
        Set<Integer> visited = new HashSet<>();

        for (int i = 0; i < route.length; i++) {
            int current = route[i];

            if (!validTowerIds.contains(current)) return false;


            if (visited.contains(current)) return false;
            visited.add(current);


            if (i > 0) {
                int prev = route[i - 1];
                if (!towerConnections.getOrDefault(prev, Set.of()).contains(current)) return false;
            }
        }

        return true;
    }

    @Override
    public double adjustFitness(Chromosome chromosome, double originalFitness) {
        return originalFitness * 0.5;
    }
}