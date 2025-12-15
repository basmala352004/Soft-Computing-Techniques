package com.scproject.neural_network.activations;

public class Linear implements Activation {
    @Override
    public double forward(double z) {
        return z;
    }

    @Override
    public double backward(double z) {
        return 1.0;
    }

    @Override
    public String getName() {
        return "Linear";
    }
}
