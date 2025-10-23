package com.scproject.crossover;

import com.scproject.chromosome.Chromosome;
import java.util.Random;

public class UniformMethod implements CrossoverStrategy{
    Random randomNumber;

    public UniformMethod() {
        randomNumber = new Random();
    }

    @Override
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate){
        //Validate Chromosomes
        validate(parent1,parent2,crossoverRate);

        //Make Copies
        Chromosome offspring1 = parent1.clone();
        Chromosome offspring2 = parent2.clone();
        if (randomNumber.nextDouble() > crossoverRate) {
            return new Chromosome[]{offspring1, offspring2};
        }

        //Old genes
        Object[] gene1 = offspring1.getGenes();
        Object[] gene2 = offspring2.getGenes();
        int length = gene1.length;

       //New genes
       Object[] newGene1 = new Object[length];
       Object[] newGene2 = new Object[length];

       //Flip the coin
        for(int i = 0; i < length; i++) {
            double randomCoin = randomNumber.nextDouble();
            if(randomCoin <= 0.5){
                newGene1[i] = gene1[i];
                newGene2[i] = gene2[i];
            }
            else{
                newGene1[i] = gene2[i];
                newGene2[i] = gene1[i];
            }
        }

        //Update offspring
        offspring1.setGenes(newGene1);
        offspring2.setGenes(newGene2);

        return new Chromosome[]{offspring1, offspring2};
    }

    @Override
    public String getName() {
        return "Uniform Crossover";
    }
}



