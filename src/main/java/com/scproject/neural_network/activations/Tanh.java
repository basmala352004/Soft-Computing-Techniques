package com.scproject.neural_network.activations;

/**
 * Tanh activation function
 */
public class Tanh extends Activation {
    
    public Tanh() {
        super("tanh", false);
    }
    
    @Override
    public double[][] forward(double[][] x) {
        double[][] result = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                result[i][j] = Math.tanh(x[i][j]);
            }
        }
        return result;
    }
    
    @Override
    public double[][] derivative(double[][] output) {
        double[][] result = new double[output.length][output[0].length];
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                result[i][j] = 1.0 - output[i][j] * output[i][j];
            }
        }
        return result;
    }
}