package com.scproject.fitness;

import com.scproject.chromosome.Chromosome;
import com.scproject.chromosome.IntegerChromosome;

import java.util.Map;
import java.util.Set;


public class RoutingFitnessFunction implements FitnessFunction {

    private final Map<Integer, Set<Integer>> towerConnections;
    private final Map<Integer, Double> towerThroughput;
    private final Map<Integer, Map<Integer, Double>> towerDistance;

    public RoutingFitnessFunction(
            Map<Integer, Set<Integer>> towerConnections,
            Map<Integer, Double> towerThroughput,
            Map<Integer, Map<Integer, Double>> towerDistance
    ) {
        this.towerConnections = towerConnections;
        this.towerThroughput = towerThroughput;
        this.towerDistance = towerDistance;
    }

    @Override
    public double evaluate(Chromosome chromosome) {
        if (!(chromosome instanceof IntegerChromosome)) {
            throw new IllegalArgumentException("RoutingFitnessFunction only supports IntegerChromosome");
        }

        Integer[] route = (Integer[]) chromosome.getGenes();

        double totalThroughput = 0.0;
        double totalDistance = 0.0;
        int validConnections = 0;

        for (int i = 0; i < route.length - 1; i++) {
            int from = route[i];
            int to = route[i + 1];

            if (towerConnections.getOrDefault(from, Set.of()).contains(to)) {
                validConnections++;

                double avgThroughput = (towerThroughput.getOrDefault(from, 0.0)
                        + towerThroughput.getOrDefault(to, 0.0)) / 2.0;
                totalThroughput += avgThroughput;

                totalDistance += towerDistance.getOrDefault(from, Map.of())
                        .getOrDefault(to, 1.0);
            } else {
                //Penalize invalid link
                totalDistance += 5.0;
                totalThroughput -= 10.0;
            }
        }

        if (validConnections == 0) return 0.0;

        double avgThroughput = totalThroughput / validConnections;
        double avgDistance = totalDistance / validConnections;

        //throughput favors higher values, distance penalizes longer links, scale *10
        double ratio = avgThroughput / (avgThroughput + avgDistance * 10);
        double fitnessPercent = ratio * 100.0;

        return Math.max(fitnessPercent, 0.0);
    }
}
