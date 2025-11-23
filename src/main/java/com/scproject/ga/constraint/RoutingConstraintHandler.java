package com.scproject.ga.constraint;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.chromosome.IntegerChromosome;

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

        IntegerChromosome intChrom = (IntegerChromosome) chromosome;
        Object[] genes = intChrom.getGenes();

        Set<Object> unique = new HashSet<>(Arrays.asList(genes));

        return unique.size() == genes.length && !Arrays.asList(genes).contains(null);
    }

    @Override
    public double adjustFitness(Chromosome chromosome, double originalFitness) {
        return originalFitness * 0.5;
    }

    @Override
    public Chromosome repair(Chromosome chromosome) {
        if (!(chromosome instanceof IntegerChromosome)) {
            throw new IllegalArgumentException("Repair only supports IntegerChromosome or RoutingChromosome");
        }

        IntegerChromosome intChrom = (IntegerChromosome) chromosome;
        Object[] genes = intChrom.getGenes();
        int length = genes.length;

        Set<Integer> seen = new HashSet<>();
        List<Integer> duplicates = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            Integer val = (Integer) genes[i];
            if (!seen.add(val)) {
                duplicates.add(i);
            }
        }

        List<Integer> missing = new ArrayList<>();
        for (int i = intChrom.getLowerBound(); i <= intChrom.getUpperBound(); i++) {
            if (!seen.contains(i)) {
                missing.add(i);
            }
        }

        //Shuffle missing elements for randomness
        Collections.shuffle(missing);
        for (int i = 0; i < duplicates.size() && !missing.isEmpty(); i++) {
            int index = duplicates.get(i);
            genes[index] = missing.remove(0);
        }

        //Fix nulls
        for (int i = 0; i < length && !missing.isEmpty(); i++) {
            if (genes[i] == null) {
                genes[i] = missing.remove(0);
            }
        }

        intChrom.setGenes(genes);
        intChrom.resetEvaluation();
        return intChrom;
    }
}