package com.scproject.ga.mutation;

import com.scproject.ga.chromosome.Chromosome;
import java.util.*;

public class binaryBitFlipMutation implements MutationStrategy{

    private double mutationRate;
    private final Random random = new Random();

    public binaryBitFlipMutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        Object[] genes = chromosome.getGenes();

        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                Object gene = genes[i];

                if (gene instanceof Boolean) {
                    genes[i] = !((Boolean) gene);
                } else if (gene instanceof Integer) {
                    int value = (Integer) gene;
                    genes[i] = (value == 0) ? 1 : 0;
                } else {
                    throw new IllegalArgumentException(
                            "BitFlipMutation only supports Boolean or 0/1 Integer genes"
                    );
                }
            }
        }

        chromosome.setGenes(genes);
        chromosome.resetEvaluation();
        return chromosome;
    }

    @Override
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public boolean isApplicable(Chromosome chromosome) {
        return MutationStrategy.super.isApplicable(chromosome);
    }
}
