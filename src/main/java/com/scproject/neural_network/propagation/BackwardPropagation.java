package com.scproject.neural_network.propagation;

import com.scproject.neural_network.core.Layer;

import java.util.List;

/**
 * Backward propagation implementation
 */
public class BackwardPropagation {
    
    /**
     * Perform backward propagation through all layers
     */
    public static void backward(double[][] lossGradient, List<Layer> layers, 
                               double learningRate, double l2Regularization) {
        double[][] grad = lossGradient;
        for (int i = layers.size() - 1; i >= 0; i--) {
            grad = layers.get(i).backward(grad, learningRate, l2Regularization);
        }
    }
    
    /**
     * Backward propagation without L2 regularization
     */
    public static void backward(double[][] lossGradient, List<Layer> layers, double learningRate) {
        backward(lossGradient, layers, learningRate, 0.0);
    }
}