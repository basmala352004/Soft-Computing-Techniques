package com.scproject.neural_network.initializers;

/**
 * He initialization (for ReLU networks)
 */
public class HeInitializer extends WeightInitializer {
    
    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double std = Math.sqrt(2.0 / inputSize);
        double[][] weights = new double[inputSize][outputSize];
        
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = random.nextGaussian() * std;
            }
        }
        return weights;
    }
}