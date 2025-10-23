package com.scproject.fitness;

import com.scproject.chromosome.Chromosome;

@FunctionalInterface
public interface FitnessFunction {

    double evaluate(Chromosome chromosome);
}
