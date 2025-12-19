package com.scproject.neural_network.initializers;

import java.util.Random;

/**
 * Base class for weight initialization strategies
 */
public abstract class WeightInitializer {
    protected static final Random random = new Random();
    
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }
    
    public abstract double[][] initialize(int inputSize, int outputSize);
    
    // Factory method
    public static WeightInitializer create(String name) {
        switch (name.toLowerCase()) {
            case "uniform":
                return new RandomUniform();
            case "xavier":
                return new XavierInitializer();
            case "he":
                return new HeInitializer();
            default:
                throw new IllegalArgumentException("Unknown initializer: " + name);
        }
    }
}