package com.scproject.chromosome;

import java.util.Random;


public class FPChromosome extends Chromosome {
    private Double[] genes;
    private double lowerBound;
    private double upperBound;


    public FPChromosome(int length) {
        this(length, 0.0, 1.0);
    }

    public FPChromosome(int length, double lowerBound, double upperBound) {
        super(length);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        genes = new Double[length];
    }

    @Override
    public void initialize() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            genes[i] = lowerBound + (upperBound - lowerBound) * random.nextDouble();
        }
    }

    @Override
    public Object getGene(int index) {
        return genes[index];
    }

    @Override
    public void setGene(int index, Object value) {
        genes[index] = ((Number) value).doubleValue();
    }

    @Override
    public Object[] getGenes() {
        return genes;
    }

    @Override
    public void setGenes(Object[] genes) {
        this.genes = new Double[genes.length];
        for (int i = 0; i < genes.length; i++) {
            this.genes[i] = ((Number) genes[i]).doubleValue();
        }
    }

    @Override
    public Chromosome createNew(int length) {
        return new FPChromosome(length, lowerBound, upperBound);
    }

    @Override
    public Chromosome clone() {
        FPChromosome copy = new FPChromosome(length, lowerBound, upperBound);
        copy.setGenes(this.getGenes().clone());
        copy.setFitness(this.getFitness());
        copy.setEvaluated(this.isEvaluated());
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Double g : genes) {
            sb.append(String.format("%.4f ", g));
        }
        sb.append("]");
        return sb.toString();
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setBounds(double lower, double upper) {
        this.lowerBound = lower;
        this.upperBound = upper;
    }
}
