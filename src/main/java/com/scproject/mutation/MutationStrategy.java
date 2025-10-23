package com.scproject.mutation;


import com.scproject.chromosome.Chromosome;


public interface MutationStrategy {

    Chromosome mutate(Chromosome chromosome);


    default void setMutationRate(double mutationRate) {
        //Default,strategies can override
    }

    default boolean isApplicable(Chromosome chromosome) {
        return true;
    }
}
