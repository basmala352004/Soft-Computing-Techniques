package com.scproject.neural_network.propagation;

import com.scproject.neural_network.core.Layer;

import java.util.List;

/**
 * Forward propagation implementation
 */
public class ForwardPropagation {
    
    /**
     * Perform forward propagation through all layers
     */
    public static double[][] forward(double[][] input, List<Layer> layers) {
        double[][] output = input;
        for (Layer layer : layers) {
            output = layer.forward(output);
        }
        return output;
    }
    
    /**
     * Forward propagation with training mode control
     */
    public static double[][] forward(double[][] input, List<Layer> layers, boolean training) {
        // Set training mode for all layers
        for (Layer layer : layers) {
            layer.setTraining(training);
        }
        return forward(input, layers);
    }
}