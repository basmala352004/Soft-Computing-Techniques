package com.scproject.neural_network.activations;

/**
 * Sigmoid activation function
 */
public class Sigmoid extends Activation {
    
    public Sigmoid() {
        super("sigmoid", false);
    }
    
    @Override
    public double[][] forward(double[][] x) {
        double[][] result = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                result[i][j] = 1.0 / (1.0 + Math.exp(-x[i][j]));
            }
        }
        return result;
    }
    
    @Override
    public double[][] derivative(double[][] output) {
        double[][] result = new double[output.length][output[0].length];
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                result[i][j] = output[i][j] * (1.0 - output[i][j]);
            }
        }
        return result;
    }
}