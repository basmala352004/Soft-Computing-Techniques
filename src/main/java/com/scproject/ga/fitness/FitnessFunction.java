package com.scproject.ga.fitness;

import com.scproject.ga.chromosome.Chromosome;

@FunctionalInterface
public interface FitnessFunction {

    double evaluate(Chromosome chromosome);
}
