package com.scproject;

import com.scproject.chromosome.IntegerChromosome;
import com.scproject.core.GeneticAlgorithm;
import com.scproject.core.GAConfiguration;
import com.scproject.crossover.UniformMethod;
import com.scproject.fitness.RoutingFitnessFunction;
import com.scproject.constraint.RoutingConstraintHandler;
import com.scproject.selection.rankSelection;
import com.scproject.mutation.*;

import com.scproject.replacement.ElitistReplacement;
import com.scproject.chromosome.Chromosome;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Genetic Algorithm for Tower Routing ===");
        System.out.println("Select input mode:");
        System.out.println("1. Default (preset values)");
        System.out.println("2. Interactive (custom input)");
        System.out.print("Enter choice (1 or 2): ");

        int choice = scanner.nextInt();

        if (choice == 1) {
            runDefaultMode();
        } else if (choice == 2) {
            runInteractiveMode(scanner);
        } else {
            System.out.println("Invalid choice. Running default mode.");
            runDefaultMode();
        }

        scanner.close();
    }

    private static void runDefaultMode() {
        System.out.println("\n=== Running in DEFAULT mode ===\n");

        int nTowers = 6;
        Set<Integer> towerIds = new HashSet<>();
        for (int i = 0; i < nTowers; i++) towerIds.add(i);

        Map<Integer, Set<Integer>> connections = new HashMap<>();
        for (int i = 0; i < nTowers; i++) connections.put(i, new HashSet<>());

        Map<Integer, Map<Integer, Double>> towerDistance = new HashMap<>();

        connect(connections, 0, 1);
        connect(connections, 0, 2);
        connect(connections, 1, 2);
        connect(connections, 1, 3);
        connect(connections, 2, 4);
        connect(connections, 3, 4);
        connect(connections, 3, 5);
        connect(connections, 4, 5);

        for (int i = 0; i < nTowers; i++) {
            towerDistance.put(i, new HashMap<>());
        }

        towerDistance.get(0).put(1, 4.5);
        towerDistance.get(1).put(0, 4.5);
        towerDistance.get(0).put(2, 3.2);
        towerDistance.get(2).put(0, 3.2);
        towerDistance.get(1).put(2, 2.1);
        towerDistance.get(2).put(1, 2.1);
        towerDistance.get(1).put(3, 5.0);
        towerDistance.get(3).put(1, 5.0);
        towerDistance.get(2).put(4, 4.3);
        towerDistance.get(4).put(2, 4.3);
        towerDistance.get(3).put(4, 3.8);
        towerDistance.get(4).put(3, 3.8);
        towerDistance.get(3).put(5, 2.7);
        towerDistance.get(5).put(3, 2.7);
        towerDistance.get(4).put(5, 3.5);
        towerDistance.get(5).put(4, 3.5);

        Map<Integer, Double> towerLoad = new HashMap<>();
        towerLoad.put(0, 0.2);
        towerLoad.put(1, 0.5);
        towerLoad.put(2, 0.1);
        towerLoad.put(3, 0.6);
        towerLoad.put(4, 0.3);
        towerLoad.put(5, 0.4);

        GAConfiguration config = new GAConfiguration();
        config.setPopulationSize(8);
        config.setGenerations(120);
        config.setChromosomeLength(nTowers);
        config.setNumberOfParents(40);
        config.setCrossoverRate(0.9);
        config.setMutationRate(0.2);
        config.setVerbose(true);
        config.setPrintFrequency(10);

        runGA(config, nTowers, towerIds, connections, towerLoad, towerDistance);
    }

    private static void runInteractiveMode(Scanner scanner) {
        System.out.println("\n=== Running in INTERACTIVE mode ===\n");

        System.out.print("Enter number of towers: ");
        int nTowers = scanner.nextInt();

        Set<Integer> towerIds = new HashSet<>();
        for (int i = 0; i < nTowers; i++) towerIds.add(i);

        Map<Integer, Set<Integer>> connections = new HashMap<>();
        for (int i = 0; i < nTowers; i++) connections.put(i, new HashSet<>());

        Map<Integer, Map<Integer, Double>> towerDistance = new HashMap<>();
        for (int i = 0; i < nTowers; i++) {
            towerDistance.put(i, new HashMap<>());
        }

        System.out.print("\nEnter number of connections: ");
        int nConnections = scanner.nextInt();

        System.out.println("Enter connections (format: tower1 tower2 distance):");
        for (int i = 0; i < nConnections; i++) {
            System.out.print("Connection " + (i + 1) + ": ");
            int t1 = scanner.nextInt();
            int t2 = scanner.nextInt();
            double dist = scanner.nextDouble();

            connect(connections, t1, t2);
            towerDistance.get(t1).put(t2, dist);
            towerDistance.get(t2).put(t1, dist);
        }

        Map<Integer, Double> towerLoad = new HashMap<>();
        System.out.println("\nEnter tower loads (0.0 to 1.0):");
        for (int i = 0; i < nTowers; i++) {
            System.out.print("Tower " + i + " load: ");
            double load = scanner.nextDouble();
            towerLoad.put(i, load);
        }

        System.out.println("\n=== GA Configuration ===");

        System.out.print("Enter population size: ");
        int popSize = scanner.nextInt();

        System.out.print("Enter number of generations: ");
        int generations = scanner.nextInt();

        int numParents = popSize;

        System.out.print("Enter crossover rate (0.0 to 1.0): ");
        double crossoverRate = scanner.nextDouble();

        System.out.print("Enter mutation rate (0.0 to 1.0): ");
        double mutationRate = scanner.nextDouble();

        System.out.print("Enable verbose output? (true/false): ");
        boolean verbose = scanner.nextBoolean();

        int printFreq = 10;
        if (verbose) {
            System.out.print("Enter print frequency: ");
            printFreq = scanner.nextInt();
        }

        GAConfiguration config = new GAConfiguration();
        config.setPopulationSize(popSize);
        config.setGenerations(generations);
        config.setChromosomeLength(nTowers);
        config.setNumberOfParents(numParents);
        config.setCrossoverRate(crossoverRate);
        config.setMutationRate(mutationRate);
        config.setVerbose(verbose);
        config.setPrintFrequency(printFreq);

        runGA(config, nTowers, towerIds, connections, towerLoad, towerDistance);
    }

    private static void runGA(GAConfiguration config, int nTowers, Set<Integer> towerIds,
                              Map<Integer, Set<Integer>> connections, Map<Integer, Double> towerLoad,
                              Map<Integer, Map<Integer, Double>> towerDistance) {

        IntegerChromosome chromosome = new IntegerChromosome(nTowers);
        config.setChromosomePrototype(chromosome);

        config.setSelectionStrategy(new rankSelection());
        config.setCrossoverStrategy(new UniformMethod());

        SwapMutation swapMutation = new SwapMutation();
        swapMutation.setMutationRate(0.5);
        InversionMutation inversionMutation = new InversionMutation();
        inversionMutation.setMutationRate(0.2);
        config.setMutationStrategy(inversionMutation);

        ElitistReplacement replacement = new ElitistReplacement();
        replacement.setEliteCount(2);
        config.setReplacementStrategy(replacement);

        RoutingFitnessFunction fitnessFunction = new RoutingFitnessFunction(connections, towerLoad, towerDistance);
        RoutingConstraintHandler constraintHandler = new RoutingConstraintHandler(towerIds, connections);

        GeneticAlgorithm ga = new GeneticAlgorithm(config);
        ga.setFitnessFunction(fitnessFunction);
        ga.setConstraintHandler(constraintHandler);
        ga.setSelectionStrategy(config.getSelectionStrategy());
        ga.setCrossoverStrategy(config.getCrossoverStrategy());
        ga.setMutationStrategy(config.getMutationStrategy());
        ga.setReplacementStrategy(config.getReplacementStrategy());
        ga.setChromosomePrototype(config.getChromosomePrototype());
        ga.setVerbose(config.isVerbose());
        ga.setPrintFrequency(config.getPrintFrequency());

        System.out.println("\nStarting GA for Routing (towers = " + nTowers + ") ...");
        ga.run();

        Chromosome best = ga.getBestSolution();
        System.out.println("Best route: " + best);
    }

    private static void connect(Map<Integer, Set<Integer>> m, int a, int b) {
        m.get(a).add(b);
        m.get(b).add(a);
    }
}