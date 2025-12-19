package com.scproject.neural_network.loss;

import com.scproject.neural_network.core.MatrixUtils;

/**
 * Mean Squared Error loss function
 */
public class MSELoss extends LossFunction {
    
    @Override
    public double forward(double[][] predictions, double[][] targets) {
        if (predictions.length != targets.length || 
            predictions[0].length != targets[0].length) {
            throw new IllegalArgumentException("Predictions and targets must have same shape");
        }
        
        this.cachedPredictions = MatrixUtils.copy(predictions);
        this.cachedTargets = MatrixUtils.copy(targets);
        
        double sum = 0.0;
        int count = 0;
        
        for (int i = 0; i < predictions.length; i++) {
            for (int j = 0; j < predictions[0].length; j++) {
                double diff = predictions[i][j] - targets[i][j];
                sum += diff * diff;
                count++;
            }
        }
        
        return sum / count;
    }
    
    @Override
    public double[][] backward() {
        if (cachedPredictions == null || cachedTargets == null) {
            throw new IllegalStateException("Forward pass must be called before backward pass");
        }
        
        int n = cachedPredictions.length;
        double[][] gradient = new double[cachedPredictions.length][cachedPredictions[0].length];
        
        for (int i = 0; i < cachedPredictions.length; i++) {
            for (int j = 0; j < cachedPredictions[0].length; j++) {
                gradient[i][j] = (2.0 / n) * (cachedPredictions[i][j] - cachedTargets[i][j]);
            }
        }
        
        return gradient;
    }
}