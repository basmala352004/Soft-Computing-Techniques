package com.scproject.neural_network.propagation;

import com.scproject.neural_network.core.MatrixUtils;

/**
 * Cache for storing intermediate values during propagation
 */
public class PropagationCache {
    private double[][] cachedInput;
    private double[][] cachedZ;
    private double[][] cachedActivation;
    
    public void cacheInput(double[][] input) {
        this.cachedInput = MatrixUtils.copy(input);
    }
    
    public void cacheZ(double[][] z) {
        this.cachedZ = MatrixUtils.copy(z);
    }
    
    public void cacheActivation(double[][] activation) {
        this.cachedActivation = MatrixUtils.copy(activation);
    }
    
    public double[][] getCachedInput() {
        return cachedInput;
    }
    
    public double[][] getCachedZ() {
        return cachedZ;
    }
    
    public double[][] getCachedActivation() {
        return cachedActivation;
    }
    
    public boolean isValid() {
        return cachedInput != null && cachedZ != null;
    }
    
    public void clear() {
        cachedInput = null;
        cachedZ = null;
        cachedActivation = null;
    }
}