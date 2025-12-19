package com.scproject.neural_network.core;

import com.scproject.neural_network.activations.Activation;
import com.scproject.neural_network.initializers.WeightInitializer;
import com.scproject.neural_network.propagation.PropagationCache;

/**
 * Fully connected (dense) layer implementation
 */
public class DenseLayer implements Layer {
    private final int inputSize;
    private final int outputSize;
    private final Activation activation;
    private double[][] weights;
    private double[] bias;
    private Dropout dropout;
    
    // Cached values for backpropagation
    private final PropagationCache cache;
    
    public DenseLayer(int inputSize, int outputSize, String activationName, String initializerName) {
        if (inputSize <= 0 || outputSize <= 0) {
            throw new IllegalArgumentException("Input and output sizes must be positive");
        }
        
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activation = Activation.create(activationName);
        this.dropout = null; // No dropout by default
        this.cache = new PropagationCache();
        
        // Initialize weights and bias
        WeightInitializer initializer = WeightInitializer.create(initializerName);
        this.weights = initializer.initialize(inputSize, outputSize);
        this.bias = new double[outputSize];
    }
    
    public DenseLayer(int inputSize, int outputSize, String activationName, String initializerName, double dropoutRate) {
        this(inputSize, outputSize, activationName, initializerName);
        if (dropoutRate > 0.0) {
            this.dropout = new Dropout(dropoutRate, 42);
        }
    }
    
    @Override
    public double[][] forward(double[][] input) {
        if (input[0].length != inputSize) {
            throw new IllegalArgumentException(
                String.format("Input features %d do not match expected %d", 
                            input[0].length, inputSize));
        }
        
        // Cache input for backpropagation
        cache.cacheInput(input);
        
        // Compute z = X * W + b
        double[][] z = MatrixUtils.add(MatrixUtils.multiply(input, weights), bias);
        cache.cacheZ(z);
        
        // Apply activation function
        double[][] activated = activation.forward(z);
        cache.cacheActivation(activated);
        
        // Apply dropout if configured
        if (dropout != null) {
            return dropout.forward(activated);
        }
        
        return activated;
    }
    
    @Override
    public double[][] backward(double[][] gradOutput, double learningRate, double l2Regularization) {
        if (!cache.isValid()) {
            throw new IllegalStateException("Forward pass must be called before backward pass");
        }
        
        // Apply dropout backward pass
        double[][] gradAfterDropout = gradOutput;
        if (dropout != null) {
            gradAfterDropout = dropout.backward(gradOutput);
        }
        
        // Compute activation gradient
        double[][] activationGrad;
        if (activation.usesPreActivation()) {
            activationGrad = activation.derivative(cache.getCachedZ());
        } else {
            activationGrad = activation.derivative(cache.getCachedActivation());
        }
        
        // Compute delta = gradOutput * activationGrad
        double[][] delta = MatrixUtils.elementWiseMultiply(gradAfterDropout, activationGrad);
        
        // Compute gradient for previous layer BEFORE updating weights
        double[][] gradInput = MatrixUtils.multiply(delta, MatrixUtils.transpose(weights));
        
        // Compute gradients with L2 regularization
        double[][] gradWeights = MatrixUtils.multiply(MatrixUtils.transpose(cache.getCachedInput()), delta);
        
        // Add L2 regularization to weight gradients
        if (l2Regularization > 0) {
            gradWeights = MatrixUtils.add(gradWeights, MatrixUtils.multiply(weights, l2Regularization));
        }
        
        double[] gradBias = MatrixUtils.mean(delta, 0);
        
        // Update parameters
        weights = MatrixUtils.subtract(weights, MatrixUtils.multiply(gradWeights, learningRate));
        bias = MatrixUtils.subtract(bias, MatrixUtils.multiply(gradBias, learningRate));
        
        return gradInput;
    }
    
    @Override
    public void setTraining(boolean training) {
        if (dropout != null) {
            dropout.setTraining(training);
        }
    }
    
    @Override
    public int getParameterCount() {
        return weights.length * weights[0].length + bias.length;
    }
    
    @Override
    public int getOutputSize() {
        return outputSize;
    }
    
    @Override
    public int getInputSize() {
        return inputSize;
    }
    
    // Getters
    public double[][] getWeights() { return weights; }
    public double[] getBias() { return bias; }
    public Activation getActivation() { return activation; }
}