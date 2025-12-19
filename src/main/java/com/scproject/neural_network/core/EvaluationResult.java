package com.scproject.neural_network.core;

/**
 * Container for evaluation results
 */
public class EvaluationResult {
    private final double loss;
    private final double accuracy;
    
    public EvaluationResult(double loss, double accuracy) {
        this.loss = loss;
        this.accuracy = accuracy;
    }
    
    public double getLoss() { return loss; }
    public double getAccuracy() { return accuracy; }
    
    @Override
    public String toString() {
        return String.format("EvaluationResult{loss=%.4f, accuracy=%.4f}", loss, accuracy);
    }
}