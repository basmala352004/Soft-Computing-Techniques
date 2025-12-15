package com.scproject.neural_network.initializers;

import java.util.Random;

public class RandomUniform implements WeightInitializer {
    private double minValue;
    private double maxValue;
    private Random random;

    public RandomUniform(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.random = new Random();
    }

    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double[][] weights = new double[inputSize][outputSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = minValue + (maxValue - minValue) * random.nextDouble();
            }
        }
        return weights;
    }

    @Override
    public String getName() {
        return "RandomUniform";
    }
}
