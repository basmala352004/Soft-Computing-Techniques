package com.scproject.neural_network.initializers;

/**
 * Random uniform initialization
 */
public class RandomUniform extends WeightInitializer {
    private final double low;
    private final double high;
    
    public RandomUniform() {
        this(-0.5, 0.5);
    }
    
    public RandomUniform(double low, double high) {
        this.low = low;
        this.high = high;
    }
    
    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double[][] weights = new double[inputSize][outputSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = low + (high - low) * random.nextDouble();
            }
        }
        return weights;
    }
}