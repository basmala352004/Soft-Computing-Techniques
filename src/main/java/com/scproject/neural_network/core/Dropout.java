package com.scproject.neural_network.core;

import java.util.Random;

/**
 * Dropout regularization implementation
 */
public class Dropout {
    private final double dropoutRate;
    private final Random random;
    private boolean[][] mask;
    private boolean training;
    
    public Dropout(double dropoutRate, long seed) {
        this.dropoutRate = dropoutRate;
        this.random = new Random(seed);
        this.training = true;
    }
    
    public void setTraining(boolean training) {
        this.training = training;
    }
    
    public double[][] forward(double[][] input) {
        if (!training || dropoutRate == 0.0) {
            return input;
        }
        
        int rows = input.length;
        int cols = input[0].length;
        mask = new boolean[rows][cols];
        double[][] output = new double[rows][cols];
        
        double scale = 1.0 / (1.0 - dropoutRate);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mask[i][j] = random.nextDouble() > dropoutRate;
                output[i][j] = mask[i][j] ? input[i][j] * scale : 0.0;
            }
        }
        
        return output;
    }
    
    public double[][] backward(double[][] gradOutput) {
        if (!training || dropoutRate == 0.0 || mask == null) {
            return gradOutput;
        }
        
        double[][] gradInput = new double[gradOutput.length][gradOutput[0].length];
        double scale = 1.0 / (1.0 - dropoutRate);
        
        for (int i = 0; i < gradOutput.length; i++) {
            for (int j = 0; j < gradOutput[0].length; j++) {
                gradInput[i][j] = mask[i][j] ? gradOutput[i][j] * scale : 0.0;
            }
        }
        
        return gradInput;
    }
}