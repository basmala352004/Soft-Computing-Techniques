package com.scproject.neural_network.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Data preprocessing and manipulation utilities
 */
public class DataUtils {
    private static final Random random = new Random();
    
    /**
     * Split data into training and testing sets
     */
    public static class TrainTestSplit {
        public final double[][] XTrain;
        public final double[][] XTest;
        public final double[][] yTrain;
        public final double[][] yTest;
        
        public TrainTestSplit(double[][] XTrain, double[][] XTest, 
                             double[][] yTrain, double[][] yTest) {
            this.XTrain = XTrain;
            this.XTest = XTest;
            this.yTrain = yTrain;
            this.yTest = yTest;
        }
    }
    
    public static TrainTestSplit trainTestSplit(double[][] X, double[][] y, 
                                               double testSize, boolean shuffle, Integer seed) {
        if (X.length != y.length) {
            throw new IllegalArgumentException("X and y must have same number of samples");
        }
        if (testSize <= 0 || testSize >= 1) {
            throw new IllegalArgumentException("Test size must be between 0 and 1");
        }
        
        if (seed != null) {
            random.setSeed(seed);
        }
        
        // Create indices
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < X.length; i++) {
            indices.add(i);
        }
        
        if (shuffle) {
            Collections.shuffle(indices, random);
        }
        
        // Split indices
        int splitIdx = (int) (X.length * (1 - testSize));
        List<Integer> trainIndices = indices.subList(0, splitIdx);
        List<Integer> testIndices = indices.subList(splitIdx, indices.size());
        
        // Create splits
        double[][] XTrain = new double[trainIndices.size()][X[0].length];
        double[][] XTest = new double[testIndices.size()][X[0].length];
        double[][] yTrain = new double[trainIndices.size()][y[0].length];
        double[][] yTest = new double[testIndices.size()][y[0].length];
        
        for (int i = 0; i < trainIndices.size(); i++) {
            int idx = trainIndices.get(i);
            System.arraycopy(X[idx], 0, XTrain[i], 0, X[0].length);
            System.arraycopy(y[idx], 0, yTrain[i], 0, y[0].length);
        }
        
        for (int i = 0; i < testIndices.size(); i++) {
            int idx = testIndices.get(i);
            System.arraycopy(X[idx], 0, XTest[i], 0, X[0].length);
            System.arraycopy(y[idx], 0, yTest[i], 0, y[0].length);
        }
        
        return new TrainTestSplit(XTrain, XTest, yTrain, yTest);
    }
    
    /**
     * Standardization result container
     */
    public static class StandardizationResult {
        public final double[][] standardized;
        public final double[] mean;
        public final double[] std;
        
        public StandardizationResult(double[][] standardized, double[] mean, double[] std) {
            this.standardized = standardized;
            this.mean = mean;
            this.std = std;
        }
    }
    
    /**
     * Standardize features (z-score normalization)
     */
    public static StandardizationResult standardize(double[][] X) {
        int numSamples = X.length;
        int numFeatures = X[0].length;
        
        // Calculate mean
        double[] mean = new double[numFeatures];
        for (int j = 0; j < numFeatures; j++) {
            double sum = 0.0;
            for (int i = 0; i < numSamples; i++) {
                sum += X[i][j];
            }
            mean[j] = sum / numSamples;
        }
        
        // Calculate standard deviation (using N-1 for sample std like NumPy)
        double[] std = new double[numFeatures];
        for (int j = 0; j < numFeatures; j++) {
            double sumSquares = 0.0;
            for (int i = 0; i < numSamples; i++) {
                double diff = X[i][j] - mean[j];
                sumSquares += diff * diff;
            }
            // Use sample standard deviation (N-1) to match NumPy's behavior
            std[j] = Math.sqrt(sumSquares / Math.max(1, numSamples - 1));
            if (std[j] == 0 || std[j] < 1e-8) std[j] = 1.0; // Avoid division by zero
        }
        
        // Standardize
        double[][] standardized = new double[numSamples][numFeatures];
        for (int i = 0; i < numSamples; i++) {
            for (int j = 0; j < numFeatures; j++) {
                standardized[i][j] = (X[i][j] - mean[j]) / std[j];
            }
        }
        
        return new StandardizationResult(standardized, mean, std);
    }
    
    /**
     * Apply standardization using existing mean and std
     */
    public static double[][] applyStandardization(double[][] X, double[] mean, double[] std) {
        double[][] result = new double[X.length][X[0].length];
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < X[0].length; j++) {
                result[i][j] = (X[i][j] - mean[j]) / std[j];
            }
        }
        return result;
    }
    
    /**
     * Convert integer labels to one-hot encoding
     */
    public static double[][] toOneHot(int[] labels, int numClasses) {
        double[][] oneHot = new double[labels.length][numClasses];
        for (int i = 0; i < labels.length; i++) {
            oneHot[i][labels[i]] = 1.0;
        }
        return oneHot;
    }
    
    /**
     * Convert one-hot encoding back to integer labels
     */
    public static int[] fromOneHot(double[][] oneHot) {
        int[] labels = new int[oneHot.length];
        for (int i = 0; i < oneHot.length; i++) {
            int maxIdx = 0;
            for (int j = 1; j < oneHot[0].length; j++) {
                if (oneHot[i][j] > oneHot[i][maxIdx]) {
                    maxIdx = j;
                }
            }
            labels[i] = maxIdx;
        }
        return labels;
    }
    
    /**
     * Handle missing values with mean imputation
     */
    public static double[][] imputeMissingValues(double[][] X, double missingValue) {
        double[][] result = new double[X.length][X[0].length];
        
        // Calculate column means (ignoring missing values)
        double[] means = new double[X[0].length];
        for (int j = 0; j < X[0].length; j++) {
            double sum = 0.0;
            int count = 0;
            for (int i = 0; i < X.length; i++) {
                if (X[i][j] != missingValue && !Double.isNaN(X[i][j])) {
                    sum += X[i][j];
                    count++;
                }
            }
            means[j] = count > 0 ? sum / count : 0.0;
        }
        
        // Replace missing values with means
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < X[0].length; j++) {
                if (X[i][j] == missingValue || Double.isNaN(X[i][j])) {
                    result[i][j] = means[j];
                } else {
                    result[i][j] = X[i][j];
                }
            }
        }
        
        return result;
    }
}