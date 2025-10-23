package com.scproject.constraint;


import com.scproject.chromosome.Chromosome;

public interface ConstraintHandler {


    boolean isFeasible(Chromosome chromosome);


    double adjustFitness(Chromosome chromosome, double originalFitness);


    default double getViolationMeasure(Chromosome chromosome) {
        return 0.0;
    }


    Chromosome repair(Chromosome chromosome);
}
