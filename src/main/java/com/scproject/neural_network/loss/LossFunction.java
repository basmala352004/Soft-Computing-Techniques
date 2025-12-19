package com.scproject.neural_network.loss;

/**
 * Base class for loss functions
 */
public abstract class LossFunction {
    protected double[][] cachedPredictions;
    protected double[][] cachedTargets;
    
    public abstract double forward(double[][] predictions, double[][] targets);
    public abstract double[][] backward();
    
    // Factory method
    public static LossFunction create(String name) {
        switch (name.toLowerCase()) {
            case "mse":
                return new MSELoss();
            case "cross_entropy":
                return new CrossEntropyLoss();
            default:
                throw new IllegalArgumentException("Unknown loss function: " + name);
        }
    }
}