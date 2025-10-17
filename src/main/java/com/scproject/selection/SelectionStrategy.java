package com.scproject.selection;


import com.scproject.chromosome.Chromosome;
import com.scproject.core.Population;

/**
 * Interface for different selection strategies in genetic algorithms.
 * Selection determines which individuals are chosen for reproduction.
 */
public interface SelectionStrategy {

    /**
     * Select a single individual from the population.
     *
     * @param population The population to select from
     * @return The selected chromosome
     */
    Chromosome select(Population population);

    /**
     * Select multiple individuals from the population.
     * Default implementation calls select() multiple times.
     *
     * @param population The population to select from
     * @param count Number of individuals to select
     * @return Array of selected chromosomes
     */
    default Chromosome[] select(Population population, int count) {
        Chromosome[] selected = new Chromosome[count];
        for (int i = 0; i < count; i++) {
            selected[i] = select(population);
        }
        return selected;
    }
}
