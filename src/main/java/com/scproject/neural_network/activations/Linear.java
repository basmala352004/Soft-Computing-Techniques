package com.scproject.neural_network.activations;

import com.scproject.neural_network.core.MatrixUtils;

/**
 * Linear activation function
 */
public class Linear extends Activation {
    
    public Linear() {
        super("linear", false);
    }
    
    @Override
    public double[][] forward(double[][] x) {
        return MatrixUtils.copy(x);
    }
    
    @Override
    public double[][] derivative(double[][] x) {
        double[][] result = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                result[i][j] = 1.0;
            }
        }
        return result;
    }
}