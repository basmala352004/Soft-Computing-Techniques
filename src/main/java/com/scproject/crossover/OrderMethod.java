package com.scproject.crossover;

import com.scproject.chromosome.Chromosome;
import com.scproject.constraint.ConstraintHandler;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrderMethod implements CrossoverStrategy{
    Random randomNumber;

    public OrderMethod() {
        randomNumber = new Random();
    }

    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate, ConstraintHandler constraintHandler) {
        //Validate Chromosomes
        validate(parent1,parent2,crossoverRate);
        //Make Copies
        Chromosome offspring1 = parent1.clone();
        Chromosome offspring2 = parent2.clone();

        if (randomNumber.nextDouble() > crossoverRate) {
            return new Chromosome[]{offspring1, offspring2};
        }
        //Perform Order method for offspring1(using parent1 and parent2)
        doOrderMethod(offspring1, parent1, parent2);
        //Perform order method for offspring2(using parent2 and parent1)
        doOrderMethod(offspring2, parent2, parent1);
        return new Chromosome[]{offspring1, offspring2};
    }

    //Helper method to perform Order method for one offspring
    void doOrderMethod(Chromosome offspring, Chromosome parent1, Chromosome parent2){
        Object[] p1Genes = parent1.getGenes();
        Object[] p2Genes = parent2.getGenes();
        int length = p1Genes.length;

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

        //Create offspring array and a set to track used values
        Object[] offspringGenes = new Object[length];
        Set<Object> usedValues = new HashSet<>();

        //Copy the segment from Parent 1 to offspring
        for(int i = point1; i <= point2; i++) {
            offspringGenes[i] = p1Genes[i];
            usedValues.add(p1Genes[i]);
        }

        //Fill remaining positions from Parent2 in order
        int currentPos = (point2 + 1) % length;//Start after segment,then complete around
        for(int i = 0; i < length; i++) {
            int p2Index = (point2 + 1 + i) % length;//Go through parent2 starting after segment
            Object value = p2Genes[p2Index];
            //add values only when not in the copied segment
            if (!usedValues.contains(value)) {
                offspringGenes[currentPos] = value;
                currentPos = (currentPos + 1) % length;//Move to next position
            }
        }

        //Update offspring with new genes
        offspring.setGenes(offspringGenes);
        }

    @Override
    public String getName() {
        return "Order Crossover";
    }

}
