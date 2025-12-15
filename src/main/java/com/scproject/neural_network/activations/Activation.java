package com.scproject.neural_network.activations;

public interface Activation {

    double forward(double z);
    double backward(double z);
    String getName();
}
