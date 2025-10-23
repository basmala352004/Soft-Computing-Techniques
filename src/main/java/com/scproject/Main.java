package com.scproject;

import com.scproject.chromosome.RoutingChromosome;
import com.scproject.core.GeneticAlgorithm;
import com.scproject.core.GAConfiguration;
import com.scproject.fitness.RoutingFitnessFunction;
import com.scproject.constraint.RoutingConstraintHandler;
import com.scproject.selection.rankSelection;
import com.scproject.crossover.OrderMethod;
import com.scproject.mutation.SwapMutation;
import com.scproject.replacement.ElitistReplacement;
import com.scproject.chromosome.Chromosome;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // -------------------------
        // 1) Define towers & graph
        // -------------------------
        // Example: 6 towers (0..5)
        int nTowers = 6;
        Set<Integer> towerIds = new HashSet<>();
        for (int i = 0; i < nTowers; i++) towerIds.add(i);

        // Connections: undirected example graph
        Map<Integer, Set<Integer>> connections = new HashMap<>();
        for (int i = 0; i < nTowers; i++) connections.put(i, new HashSet<>());

        // define some links (symmetric)
        connect(connections, 0, 1);
        connect(connections, 0, 2);
        connect(connections, 1, 2);
        connect(connections, 1, 3);
        connect(connections, 2, 4);
        connect(connections, 3, 4);
        connect(connections, 3, 5);
        connect(connections, 4, 5);

        // Tower load (0.0 = empty, 1.0 = full). Prefer low-load towers.
        Map<Integer, Double> towerLoad = new HashMap<>();
        towerLoad.put(0, 0.2);
        towerLoad.put(1, 0.5);
        towerLoad.put(2, 0.1);
        towerLoad.put(3, 0.6);
        towerLoad.put(4, 0.3);
        towerLoad.put(5, 0.4);

        // -------------------------
        // 2) Prepare GA components
        // -------------------------
        GAConfiguration config = new GAConfiguration();
        config.setPopulationSize(80);
        config.setGenerations(120);
        config.setChromosomeLength(nTowers);
        config.setNumberOfParents(40);
        config.setCrossoverRate(0.9);
        config.setMutationRate(0.2);
        config.setVerbose(true);
        config.setPrintFrequency(10);

        // chromosome prototype (permutation)
        RoutingChromosome prototype = new RoutingChromosome(nTowers);
        config.setChromosomePrototype(prototype);

        // selection, crossover, mutation, replacement
        config.setSelectionStrategy(new rankSelection());   // or new com.scproject.selection.rouletteStrategy()
        config.setCrossoverStrategy(new OrderMethod());
        SwapMutation swapMutation = new SwapMutation();
        swapMutation.setMutationRate(0.2);
        config.setMutationStrategy(swapMutation);
        ElitistReplacement replacement = new ElitistReplacement();
        replacement.setEliteCount(2);
        config.setReplacementStrategy(replacement);

        // -------------------------
        // 3) Fitness + Constraints
        // -------------------------
        RoutingFitnessFunction fitnessFunction = new RoutingFitnessFunction(connections, towerLoad);
        RoutingConstraintHandler constraintHandler = new RoutingConstraintHandler(towerIds, connections);

        // -------------------------
        // 4) Build GA and run
        // -------------------------
        GeneticAlgorithm ga = new GeneticAlgorithm(config);
        ga.setFitnessFunction(fitnessFunction);
        ga.setConstraintHandler(constraintHandler);

        // set strategies into GA (optional convenience setters)
        ga.setSelectionStrategy(config.getSelectionStrategy());
        ga.setCrossoverStrategy(config.getCrossoverStrategy());
        ga.setMutationStrategy(config.getMutationStrategy());
        ga.setReplacementStrategy(config.getReplacementStrategy());
        ga.setChromosomePrototype(config.getChromosomePrototype());
        ga.setVerbose(config.isVerbose());
        ga.setPrintFrequency(config.getPrintFrequency());

        System.out.println("Starting GA for Routing (towers = " + nTowers + ") ...");
        ga.run();

        System.out.println("Done. Best fitness: " + ga.getBestFitness());
        Chromosome best = ga.getBestSolution();
        System.out.println("Best route: " + best);
    }

    private static void connect(Map<Integer, Set<Integer>> m, int a, int b) {
        m.get(a).add(b);
        m.get(b).add(a);
    }
}
