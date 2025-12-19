package com.scproject.neural_network.activations;

/**
 * Base class for activation functions
 */
public abstract class Activation {
    protected final String name;
    protected final boolean usesPreActivation;
    
    protected Activation(String name, boolean usesPreActivation) {
        this.name = name;
        this.usesPreActivation = usesPreActivation;
    }
    
    public abstract double[][] forward(double[][] x);
    public abstract double[][] derivative(double[][] x);
    
    public String getName() { return name; }
    public boolean usesPreActivation() { return usesPreActivation; }
    
    // Factory method
    public static Activation create(String name) {
        switch (name.toLowerCase()) {
            case "sigmoid":
                return new Sigmoid();
            case "relu":
                return new ReLU();
            case "tanh":
                return new Tanh();
            case "linear":
                return new Linear();
            default:
                throw new IllegalArgumentException("Unknown activation: " + name);
        }
    }
}