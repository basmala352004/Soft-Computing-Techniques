package com.scproject.neural_network.core;

/**
 * Interface for neural network layers
 */
public interface Layer {
    
    /**
     * Forward propagation through this layer
     */
    double[][] forward(double[][] input);
    
    /**
     * Backward propagation through this layer
     */
    double[][] backward(double[][] gradOutput, double learningRate, double l2Regularization);
    
    /**
     * Backward propagation without L2 regularization
     */
    default double[][] backward(double[][] gradOutput, double learningRate) {
        return backward(gradOutput, learningRate, 0.0);
    }
    
    /**
     * Set training mode (affects dropout, batch norm, etc.)
     */
    void setTraining(boolean training);
    
    /**
     * Get the number of parameters in this layer
     */
    int getParameterCount();
    
    /**
     * Get the output size of this layer
     */
    int getOutputSize();
    
    /**
     * Get the input size of this layer
     */
    int getInputSize();
}