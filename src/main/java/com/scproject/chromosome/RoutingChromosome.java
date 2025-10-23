package com.scproject.chromosome;

import java.util.*;

/**
 * Chromosome representing a route (permutation) of tower indices.
 */
public class RoutingChromosome extends IntegerChromosome {

    public RoutingChromosome(int numberOfTowers) {
        super(numberOfTowers, 0, numberOfTowers - 1);
    }

    @Override
    public void initialize() {
        // Create a permutation of towers (0 .. length-1)
        Integer[] arr = new Integer[length];
        for (int i = 0; i < length; i++) arr[i] = i;
        List<Integer> list = Arrays.asList(arr);
        Collections.shuffle(list);
        setGenes(list.toArray(new Integer[0]));
    }

    @Override
    public Chromosome createNew(int length) {
        return new RoutingChromosome(length);
    }

    @Override
    public Chromosome clone() {
        RoutingChromosome copy = new RoutingChromosome(length);
        copy.setGenes(this.getGenes().clone());
        copy.setFitness(this.getFitness());
        copy.setEvaluated(this.isEvaluated());
        return copy;
    }

    @Override
    public String toString() {
        Object[] genes = getGenes();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < genes.length; i++) {
            sb.append(genes[i]);
            if (i < genes.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
