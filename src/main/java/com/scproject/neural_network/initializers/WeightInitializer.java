package com.scproject.neural_network.initializers;

public interface WeightInitializer {

    double[][] initialize(int inputSize, int outputSize);

    String getName();
}

