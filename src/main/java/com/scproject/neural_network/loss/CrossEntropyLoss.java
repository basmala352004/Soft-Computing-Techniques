package com.scproject.neural_network.loss;

import com.scproject.neural_network.core.MatrixUtils;

/**
 * Cross-Entropy loss function with softmax
 */
public class CrossEntropyLoss extends LossFunction {
    private static final double EPSILON = 1e-15;
    private double[][] cachedProbs;
    
    @Override
    public double forward(double[][] logits, double[][] targets) {
        if (logits.length != targets.length || 
            logits[0].length != targets[0].length) {
            throw new IllegalArgumentException("Logits and targets must have same shape");
        }
        
        // Apply softmax to logits with better numerical stability
        double[][] probs = softmax(logits);
        
        this.cachedProbs = probs;
        this.cachedTargets = MatrixUtils.copy(targets);
        
        // Compute cross-entropy loss with better numerical stability
        double totalLoss = 0.0;
        for (int i = 0; i < probs.length; i++) {
            double sampleLoss = 0.0;
            for (int j = 0; j < probs[0].length; j++) {
                if (targets[i][j] > 0) {
                    // Only compute loss for true classes, add epsilon for numerical stability
                    sampleLoss += targets[i][j] * Math.log(Math.max(probs[i][j], EPSILON));
                }
            }
            totalLoss -= sampleLoss;
        }
        
        return totalLoss / probs.length;
    }
    
    @Override
    public double[][] backward() {
        if (cachedProbs == null || cachedTargets == null) {
            throw new IllegalStateException("Forward pass must be called before backward pass");
        }
        
        int n = cachedProbs.length;
        double[][] gradient = new double[cachedProbs.length][cachedProbs[0].length];
        
        for (int i = 0; i < cachedProbs.length; i++) {
            for (int j = 0; j < cachedProbs[0].length; j++) {
                gradient[i][j] = (cachedProbs[i][j] - cachedTargets[i][j]) / n;
            }
        }
        
        return gradient;
    }
    
    private double[][] softmax(double[][] logits) {
        double[][] result = new double[logits.length][logits[0].length];
        
        for (int i = 0; i < logits.length; i++) {
            // Find max for numerical stability
            double max = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < logits[0].length; j++) {
                max = Math.max(max, logits[i][j]);
            }
            
            // Compute exp(x - max) with overflow protection
            double sum = 0.0;
            for (int j = 0; j < logits[0].length; j++) {
                double expVal = Math.exp(Math.min(logits[i][j] - max, 700)); // Prevent overflow
                result[i][j] = expVal;
                sum += expVal;
            }
            
            // Normalize with minimum threshold
            sum = Math.max(sum, EPSILON);
            for (int j = 0; j < logits[0].length; j++) {
                result[i][j] = Math.max(result[i][j] / sum, EPSILON);
            }
        }
        
        return result;
    }
}