package com.scproject.crossover;

import com.scproject.chromosome.Chromosome;
import java.util.Random;

public class TwoPointsMethod implements CrossoverStrategy{
    Random randomNumber;

    public TwoPointsMethod() {
        randomNumber = new Random();
    }

    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate) {

        //Validate the chromosomes
        validate(parent1,parent2,crossoverRate);

        //Create copies
        Chromosome offspring1 = parent1.clone();
        Chromosome offspring2 = parent2.clone();

        if (randomNumber.nextDouble() > crossoverRate) {
            return new Chromosome[]{offspring1, offspring2};
        }

        //Get genes
        Object[] gene1 = offspring1.getGenes();
        Object[] gene2 = offspring2.getGenes();
        int length = gene1.length;

        //Check that at least we have 3 genes so we can make 2 points method
        if(length < 3){
            return new Chromosome[]{offspring1, offspring2};//crossover can't happen :(
        }

        //Choose two random points
        int point1 = randomNumber.nextInt(length - 1) + 1;
        int point2 = randomNumber.nextInt(length - 1) + 1;

        //Validate point1 and point2
        if (point1 > point2) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }
        if (point1 == point2) {
            point2 = (point2 + 1) % length;
            if (point2 == 0) {
                point2 = 1;
            }
        }

        //Swap genes between the two points
        for (int i = point1; i < point2; i++) {
            Object temp = gene1[i];
            gene1[i] = gene2[i];
            gene2[i] = temp;
        }

        //Update offspring
        offspring1.setGenes(gene1);
        offspring2.setGenes(gene2);

        return new Chromosome[]{offspring1, offspring2};
    }

    @Override
    public String getName() {
        return "Two Point Crossover";
    }

}
