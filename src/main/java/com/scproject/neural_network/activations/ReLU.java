package com.scproject.neural_network.activations;

public class ReLU implements Activation {
    @Override
    public double forward(double z) {
        return Math.max(0, z);
    }

    @Override
    public double backward(double z) {
        return z > 0 ? 1.0 : 0.0;
    }

    @Override
    public String getName() {
        return "ReLU";
    }
}
