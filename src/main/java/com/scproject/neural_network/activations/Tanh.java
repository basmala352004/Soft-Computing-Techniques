package com.scproject.neural_network.activations;

public class Tanh implements Activation {
    @Override
    public double forward(double z) {
        return Math.tanh(z);
    }

    @Override
    public double backward(double z) {
        double t = Math.tanh(z);
        return 1 - t * t;
    }

    @Override
    public String getName() {
        return "Tanh";
    }
}
