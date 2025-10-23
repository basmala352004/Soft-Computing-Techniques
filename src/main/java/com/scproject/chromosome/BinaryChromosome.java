package com.scproject.chromosome;

import java.util.*;

public class BinaryChromosome extends Chromosome {
    private Boolean[] genes;

    public BinaryChromosome(int length) {
        super(length);
        genes = new Boolean[length];
    }

    @Override
    public void initialize() {
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            genes[i] = random.nextBoolean();
        }
    }

    @Override
    public Object getGene(int index) {
        return genes[index];
    }

    @Override
    public void setGene(int index, Object value) {
        genes[index] = (Boolean) value;
    }

    @Override
    public Object[] getGenes() {
        return genes;
    }

    @Override
    public void setGenes(Object[] genes) {
        this.genes = new Boolean[genes.length];
        for (int i = 0; i < genes.length; i++) {
            this.genes[i] = (Boolean) genes[i];
        }
    }

    @Override
    public Chromosome createNew(int length) {
        return new BinaryChromosome(length);
    }

    @Override
    public Chromosome clone() {
        BinaryChromosome copy = new BinaryChromosome(length);
        copy.setGenes(this.getGenes().clone());
        copy.setFitness(this.getFitness());
        copy.setEvaluated(this.isEvaluated());
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Boolean b : genes) {
            sb.append(b ? "1" : "0");
        }
        return sb.toString();
    }
}
