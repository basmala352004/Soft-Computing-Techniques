package com.scproject.mutation;

import com.scproject.chromosome.Chromosome;

import java.util.Random;

public class UniformFPMutation implements MutationStrategy {

    private double mutationRate = 0.05;
    private double lowerBound;
    private double upperBound;
    private final Random random = new Random();


    public UniformFPMutation(double mutationRate, double lowerBound, double upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound must be <= upperBound");
        }
        this.mutationRate = mutationRate;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        Object[] genes = chromosome.getGenes();

        for (int i = 0; i < genes.length; i++) {
            if (random.nextDouble() < mutationRate) {

                Object gene = genes[i];
                if (!(gene instanceof Number)) {
                    throw new IllegalArgumentException(
                            "UniformFPMutationExact supports only numeric genes (Double/Float). Found: "
                                    + (gene == null ? "null" : gene.getClass().getName())
                    );
                }

                double xi = ((Number) gene).doubleValue();

                double deltaL = xi - lowerBound;
                double deltaU = upperBound - xi;

                //If both deltas are <= 0 then xi is exactly at bounds and cannot move.
                if (deltaL <= 0.0 && deltaU <= 0.0) {
                    continue;
                }

                double ri1 = random.nextDouble();
                boolean goLeft = ri1 <= 0.5;

                double Delta = goLeft ? Math.max(0.0, deltaL) : Math.max(0.0, deltaU);

                if (Delta == 0.0) {
                    if (goLeft && deltaU > 0.0) {
                        goLeft = false;
                        Delta = deltaU;
                    } else if (!goLeft && deltaL > 0.0) {
                        goLeft = true;
                        Delta = deltaL;
                    } else {
                        //if both deltas are equal zero change nothing
                        continue;
                    }
                }

                double ri2 = random.nextDouble() * Delta;


                double xiNew = goLeft ? (xi - ri2) : (xi + ri2);

                genes[i] = xiNew;
            }
        }

        chromosome.setGenes(genes);
        chromosome.resetEvaluation();
        return chromosome;
    }

    @Override
    public void setMutationRate(double mutationRate) {
        if (mutationRate < 0.0 || mutationRate > 1.0) {
            throw new IllegalArgumentException("mutationRate must be in [0,1]");
        }
        this.mutationRate = mutationRate;
    }

    @Override
    public boolean isApplicable(Chromosome chromosome) {
        Object[] genes = chromosome.getGenes();
        if (genes == null || genes.length == 0) return false;
        Object g = genes[0];
        return (g instanceof Double) || (g instanceof Float) || (g instanceof Number);
    }
}
