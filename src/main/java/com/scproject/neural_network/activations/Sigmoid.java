package com.scproject.neural_network.activations;

public class Sigmoid implements Activation {
    @Override
    public double forward(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    @Override
    public double backward(double z) {
        double s = forward(z);
        return s * (1 - s);
    }

    @Override
    public String getName() {
        return "Sigmoid";
    }
}
