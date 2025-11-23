package com.scproject.ga.selection;


import com.scproject.ga.chromosome.Chromosome;
import com.scproject.ga.core.Population;


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
