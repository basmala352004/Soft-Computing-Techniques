package com.scproject.ga.crossover;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.constraint.ConstraintHandler;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrderMethod implements CrossoverStrategy{
    Random randomNumber;

    public OrderMethod() {
        randomNumber = new Random();
    }

    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate, ConstraintHandler constraintHandler) {
        validate(parent1,parent2,crossoverRate);
        Chromosome offspring1 = parent1.clone();
        Chromosome offspring2 = parent2.clone();

        if (randomNumber.nextDouble() > crossoverRate) {
            return new Chromosome[]{offspring1, offspring2};
        }
        doOrderMethod(offspring1, parent1, parent2);
        doOrderMethod(offspring2, parent2, parent1);
        return new Chromosome[]{offspring1, offspring2};
    }

    void doOrderMethod(Chromosome offspring, Chromosome parent1, Chromosome parent2){
        Object[] p1Genes = parent1.getGenes();
        Object[] p2Genes = parent2.getGenes();
        int length = p1Genes.length;

        int point1 = randomNumber.nextInt(length - 1) + 1;
        int point2 = randomNumber.nextInt(length - 1) + 1;

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

        Object[] offspringGenes = new Object[length];
        Set<Object> usedValues = new HashSet<>();

        for(int i = point1; i <= point2; i++) {
            offspringGenes[i] = p1Genes[i];
            usedValues.add(p1Genes[i]);
        }

        int currentPos = (point2 + 1) % length;
        for(int i = 0; i < length; i++) {
            int p2Index = (point2 + 1 + i) % length;
            Object value = p2Genes[p2Index];
            if (!usedValues.contains(value)) {
                offspringGenes[currentPos] = value;
                currentPos = (currentPos + 1) % length;
            }
        }

        offspring.setGenes(offspringGenes);
        }

    @Override
    public String getName() {
        return "Order Crossover";
    }

}
