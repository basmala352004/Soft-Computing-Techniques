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
        validate(parent1, parent2, crossoverRate);

        Chromosome offspring1 = parent1.clone();
        Chromosome offspring2 = parent2.clone();

        if (randomNumber.nextDouble() > crossoverRate) {
            return new Chromosome[]{offspring1, offspring2};
        }

        Object[] gene1 = offspring1.getGenes();
        Object[] gene2 = offspring2.getGenes();
        int length = gene1.length;

        if (length < numberOfPoints + 1) {
            return new Chromosome[]{offspring1, offspring2};
        }

        int[] crossoverPoints = generateUniqueCrossoverPoints(length);

        boolean swap = false;
        int startIdx = 0;

        for (int i = 0; i < crossoverPoints.length; i++) {
            int endIdx = crossoverPoints[i];

            if (swap) {
                for (int j = startIdx; j < endIdx; j++) {
                    Object temp = gene1[j];
                    gene1[j] = gene2[j];
                    gene2[j] = temp;
                }
            }

            startIdx = endIdx;
            swap = !swap;
        }

        if (swap) {
            for (int j = startIdx; j < length; j++) {
                Object temp = gene1[j];
                gene1[j] = gene2[j];
                gene2[j] = temp;
            }
        }

        offspring1.setGenes(gene1);
        offspring2.setGenes(gene2);

        return new Chromosome[]{offspring1, offspring2};
    }


    private int[] generateUniqueCrossoverPoints(int length) {
        int[] points = new int[numberOfPoints];
        boolean[] used = new boolean[length];

        for (int i = 0; i < numberOfPoints; i++) {
            int point;
            do {
                point = randomNumber.nextInt(length - 1) + 1;
            } while (used[point]);

            used[point] = true;
            points[i] = point;
        }

        Arrays.sort(points);
        return points;
    }

    @Override
    public String getName() {
        return numberOfPoints + "-Point Crossover";
    }
}