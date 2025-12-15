package com.scproject.neural_network.loss;

public interface LossFunction {

    double forward(double[][] predictions, double[][] targets);

    double[][] backward(double[][] predictions, double[][] targets);

    String getName();
}
