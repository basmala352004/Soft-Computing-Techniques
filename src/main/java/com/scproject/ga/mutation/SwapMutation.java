package com.scproject.ga.mutation;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.chromosome.IntegerChromosome;

import java.util.Random;


public class SwapMutation implements MutationStrategy {

    private final Random random = new Random();
    private double mutationRate = 0.1;

    @Override
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        if (!(chromosome instanceof IntegerChromosome)) {
            throw new IllegalArgumentException("SwapMutation only applies to IntegerChromosome");
        }

        IntegerChromosome intChrom = (IntegerChromosome) chromosome;
        Object[] genes = intChrom.getGenes();

        //if (random.nextDouble() < mutationRate) {
            int index1 = random.nextInt(genes.length);
            int index2 = random.nextInt(genes.length);

            while (index1 == index2) {
                index2 = random.nextInt(genes.length);
            }

            Object temp = genes[index1];
            genes[index1] = genes[index2];
            genes[index2] = temp;

            intChrom.setGenes(genes);
            intChrom.resetEvaluation();
        //}
        return chromosome;
    }

    @Override
    public boolean isApplicable(Chromosome chromosome) {
        return chromosome instanceof IntegerChromosome;
    }
}
