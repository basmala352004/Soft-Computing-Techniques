package com.scproject.chromosome;

import java.util.Random;

public class IntegerChromosome extends Chromosome {
    private Integer[] genes;
    private int lowerBound;
    private int upperBound;

    public IntegerChromosome(int length) {
        this(length, 0, 10);
    }

    public IntegerChromosome(int length, int lowerBound, int upperBound) {
        super(length);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        genes = new Integer[length];
    }

    @Override
    public void initialize() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            genes[i] = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
        }
    }

    @Override
    public Object getGene(int index) {
        return genes[index];
    }

    @Override
    public void setGene(int index, Object value) {
        genes[index] = ((Number) value).intValue();
    }

    @Override
    public Object[] getGenes() {
        return genes;
    }

    @Override
    public void setGenes(Object[] genes) {
        this.genes = new Integer[genes.length];
        for (int i = 0; i < genes.length; i++) {
            this.genes[i] = ((Number) genes[i]).intValue();
        }
    }

    @Override
    public Chromosome createNew(int length) {
        return new IntegerChromosome(length, lowerBound, upperBound);
    }

    @Override
    public Chromosome clone() {
        IntegerChromosome copy = new IntegerChromosome(length, lowerBound, upperBound);
        copy.setGenes(this.getGenes().clone());
        copy.setFitness(this.getFitness());
        copy.setEvaluated(this.isEvaluated());
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Integer g : genes) {
            sb.append(g).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setBounds(int lower, int upper) {
        this.lowerBound = lower;
        this.upperBound = upper;
    }
}
