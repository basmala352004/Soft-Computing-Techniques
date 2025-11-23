package com.scproject.ga.mutation;

import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.chromosome.IntegerChromosome;

import java.util.Random;

public class InversionMutation implements MutationStrategy {

    private final Random random = new Random();
    private double mutationRate = 0.1;

    @Override
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        if (!(chromosome instanceof IntegerChromosome)) {
            throw new IllegalArgumentException("InversionMutation only applies to IntegerChromosome");
        }

        IntegerChromosome intChrom = (IntegerChromosome) chromosome;
        Object[] genes = intChrom.getGenes();

        if ( genes.length > 2) {
            int point1 = random.nextInt(genes.length);
            int point2 = random.nextInt(genes.length);

            // Ensure point1 < point2
            if (point1 > point2) {
                int temp = point1;
                point1 = point2;
                point2 = temp;
            }


            while (point1 < point2) {
                Object temp = genes[point1];
                genes[point1] = genes[point2];
                genes[point2] = temp;
                point1++;
                point2--;
            }

            intChrom.setGenes(genes);
            intChrom.resetEvaluation();
        }
        return chromosome;
    }

    @Override
    public boolean isApplicable(Chromosome chromosome) {
        return chromosome instanceof IntegerChromosome;
    }
}
