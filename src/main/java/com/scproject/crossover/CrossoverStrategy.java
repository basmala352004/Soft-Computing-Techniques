package com.scproject.crossover;

import com.scproject.chromosome.Chromosome;
import com.scproject.constraint.ConstraintHandler;

public interface CrossoverStrategy {

    Chromosome[] crossover(Chromosome parent1, Chromosome parent2, double crossoverRate, ConstraintHandler constraintHandler);

    String getName();

    default void validate(Chromosome parent1, Chromosome parent2, double crossoverRate) {
        if(parent1 == null || parent2 == null){
            throw new IllegalArgumentException("Parents can't be null :( ");
        }
        if(parent1.getLength() != parent2.getLength()){
            throw new IllegalArgumentException("Parents must have the same length :( ");
        }
        if(crossoverRate < 0 || crossoverRate > 1) {
            throw new IllegalArgumentException("Crossover rate must be between 0 and 1");
        }
    }
}
