package com.scproject.neural_network.activations;

/**
 * ReLU activation function
 */
public class ReLU extends Activation {
    
    public ReLU() {
        super("relu", true);
    }
    
    @Override
    public double[][] forward(double[][] x) {
        double[][] result = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                result[i][j] = Math.max(0.0, x[i][j]);
            }
        }
        return result;
    }
    
    @Override
    public double[][] derivative(double[][] z) {
        double[][] result = new double[z.length][z[0].length];
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                result[i][j] = z[i][j] > 0 ? 1.0 : 0.0;
            }
        }
        return result;
    }
}