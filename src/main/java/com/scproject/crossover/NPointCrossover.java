package com.scproject.crossover;

import com.scproject.chromosome.Chromosome;
import com.scproject.constraint.ConstraintHandler;

import java.util.Arrays;
import java.util.Random;

public class NPointCrossover implements CrossoverStrategy {
    private Random randomNumber;
    private int numberOfPoints;

    public NPointCrossover(int numberOfPoints) {
        if (numberOfPoints < 1) {
            throw new IllegalArgumentException("Number of points must be at least 1");
        }
        this.numberOfPoints = numberOfPoints;
        this.randomNumber = new Random();
    }

    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate, ConstraintHandler constraintHandler) {
        // Validate the chromosomes
        validate(parent1, parent2, crossoverRate);

        // Create copies
        Chromosome offspring1 = parent1.clone();
        Chromosome offspring2 = parent2.clone();

        // Check if crossover should happen based on crossover rate
        if (randomNumber.nextDouble() > crossoverRate) {
            return new Chromosome[]{offspring1, offspring2};
        }

        // Get genes
        Object[] gene1 = offspring1.getGenes();
        Object[] gene2 = offspring2.getGenes();
        int length = gene1.length;

        // Check that we have enough genes for n-point crossover
        // We need at least (numberOfPoints + 1) genes
        if (length < numberOfPoints + 1) {
            return new Chromosome[]{offspring1, offspring2}; // crossover can't happen
        }

        // Generate n unique random crossover points
        int[] crossoverPoints = generateUniqueCrossoverPoints(length);

        // Perform n-point crossover
        boolean swap = false; // Start by not swapping (keep original segment)
        int startIdx = 0;

        for (int i = 0; i < crossoverPoints.length; i++) {
            int endIdx = crossoverPoints[i];

            if (swap) {
                // Swap the segment between startIdx and endIdx
                for (int j = startIdx; j < endIdx; j++) {
                    Object temp = gene1[j];
                    gene1[j] = gene2[j];
                    gene2[j] = temp;
                }
            }

            startIdx = endIdx;
            swap = !swap; // Toggle swap for next segment
        }

        // Handle the last segment (from last point to end)
        if (swap) {
            for (int j = startIdx; j < length; j++) {
                Object temp = gene1[j];
                gene1[j] = gene2[j];
                gene2[j] = temp;
            }
        }

        // Update offspring
        offspring1.setGenes(gene1);
        offspring2.setGenes(gene2);

        return new Chromosome[]{offspring1, offspring2};
    }

    /**
     * Generate n unique crossover points in the range [1, length-1]
     * Points are sorted in ascending order
     */
    private int[] generateUniqueCrossoverPoints(int length) {
        int[] points = new int[numberOfPoints];
        boolean[] used = new boolean[length];

        for (int i = 0; i < numberOfPoints; i++) {
            int point;
            do {
                // Generate point in range [1, length-1]
                point = randomNumber.nextInt(length - 1) + 1;
            } while (used[point]); // Ensure uniqueness

            used[point] = true;
            points[i] = point;
        }

        // Sort points in ascending order
        Arrays.sort(points);
        return points;
    }

    @Override
    public String getName() {
        return numberOfPoints + "-Point Crossover";
    }
}