package com.scproject.neural_network.core;

import com.scproject.neural_network.activations.Activation;
import com.scproject.neural_network.initializers.WeightInitializer;

public class Layer {
    private int inputSize;//number os inputs
    private int outputSize;//number of outputs
    private double[][] weights;//inputSize x outputSize
    private double[][] bias;//1 x outputSize
    private Activation activation;

    public Layer(int inputSize, int outputSize, Activation activation, WeightInitializer initializer) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activation = activation;
        this.weights = initializer.initialize(inputSize, outputSize);
        this.bias = new double[1][outputSize];
    }

    //GETTERS
    public double[][] getWeights() { return weights; }
    public double[][] getBias() { return bias; }
    public Activation getActivation() { return activation; }
    public int getInputSize() { return inputSize; }
    public int getOutputSize() { return outputSize; }

    //SETTERS
    public void setWeights(double[][] weights) { this.weights = weights; }
    public void setBias(double[][] bias) { this.bias = bias; }
}
