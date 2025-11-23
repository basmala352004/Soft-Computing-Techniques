package com.scproject.ga.constraint;


import com.scproject.ga.chromosome.Chromosome;

public interface ConstraintHandler {


    boolean isFeasible(Chromosome chromosome);


    double adjustFitness(Chromosome chromosome, double originalFitness);


    default double getViolationMeasure(Chromosome chromosome) {
        return 0.0;
    }


    Chromosome repair(Chromosome chromosome);
}
