package com.scproject.selection;


import com.scproject.chromosome.Chromosome;
import com.scproject.core.Population;


public interface SelectionStrategy {

    Chromosome select(Population population);


    default Chromosome[] select(Population population, int count) {
        Chromosome[] selected = new Chromosome[count];
        for (int i = 0; i < count; i++) {
            selected[i] = select(population);
        }
        return selected;
    }
}
