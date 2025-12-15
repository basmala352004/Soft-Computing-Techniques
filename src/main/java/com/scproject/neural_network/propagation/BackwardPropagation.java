package com.scproject.neural_network.propagation;

import com.scproject.neural_network.core.Layer;
import com.scproject.neural_network.activations.Activation;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/*
  1. dZ[l] = dA[l] * activation'(Z[l])         [Activation gradient]
  2. dW[l] = (1/m) * A[l-1]^T @ dZ[l]          [Weight gradient]
  3. db[l] = (1/m) * sum(dZ[l], axis=0)        [Bias gradient]
  4. dA[l-1] = dZ[l] @ W[l]^T                  [Pass gradient to previous layer]
*/
public class BackwardPropagation {

    private Map<String, double[][]> gradients;

    public BackwardPropagation() {
        this.gradients = new HashMap<>();
    }

    public Map<String, double[][]> backwardPass(double[][] dA, PropagationCache cache, List<Layer> layers) {
        //validate inputs
        if (dA == null || dA.length == 0) {
            throw new IllegalArgumentException("Gradient dA cannot be null or empty");
        }
        if (cache == null) {
            throw new IllegalArgumentException("Cache cannot be null");
        }
        if (layers == null || layers.isEmpty()) {
            throw new IllegalArgumentException("Layers list cannot be null or empty");
        }

        gradients.clear();
        int numLayers = layers.size();
        int m = cache.getInput().length;
        double[][] currentDA = dA;

        for (int i = numLayers - 1; i >= 0; i--) {
            double[][] A_prev = cache.getLayerInput(i);
            double[][] Z = cache.getLayerZ(i);
            double[][] W = cache.getLayerWeights(i);
            Activation activation = cache.getLayerActivation(i);

            double[][] dZ = activationBackward(currentDA, Z, activation);
            LinearGradients linGrads = linearBackward(dZ, A_prev, W, m);
            gradients.put("dW" + i, linGrads.dW);
            gradients.put("db" + i, linGrads.db);
            gradients.put("dA" + i, linGrads.dA_prev);

            currentDA = linGrads.dA_prev;
        }

        return gradients;
    }

    private double[][] activationBackward(double[][] dA, double[][] Z, Activation activation) {
        int rows = dA.length;
        int cols = dA[0].length;
        double[][] dZ = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double derivative = activation.backward(Z[i][j]);
                dZ[i][j] = dA[i][j] * derivative;
            }
        }

        return dZ;
    }

    private LinearGradients linearBackward(double[][] dZ, double[][] A_prev, double[][] W, int m) {
        LinearGradients grads = new LinearGradients();
        double[][] A_prev_T = transpose(A_prev);
        double[][] dW_temp = matrixMultiply(A_prev_T, dZ);
        grads.dW = scalarMultiply(dW_temp, 1.0 / m);

        int n = dZ[0].length;
        grads.db = new double[1][n];

        for (int j = 0; j < n; j++) {
            double sum = 0.0;
            for (int i = 0; i < m; i++) {
                sum += dZ[i][j];
            }
            grads.db[0][j] = sum / m;
        }

        double[][] W_T = transpose(W);
        grads.dA_prev = matrixMultiply(dZ, W_T);

        return grads;
    }

    private double[][] matrixMultiply(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        int p = B[0].length;

        // Validate dimensions
        if (n != B.length) {
            throw new IllegalArgumentException(
                    "Matrix dimensions don't match for multiplication: " +
                            "A(" + m + "x" + n + ") * B(" + B.length + "x" + p + ")"
            );
        }

        double[][] C = new double[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }

        return C;
    }

    private double[][] transpose(double[][] A) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] A_T = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                A_T[j][i] = A[i][j];
            }
        }

        return A_T;
    }

    private double[][] scalarMultiply(double[][] A, double scalar) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] * scalar;
            }
        }

        return result;
    }

    public void clipGradients(double clipValue) {
        for (String key : gradients.keySet()) {
            if (key.startsWith("dW") || key.startsWith("db")) {
                double[][] grad = gradients.get(key);
                clipMatrix(grad, clipValue);
            }
        }
    }

    private void clipMatrix(double[][] matrix, double clipValue) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] > clipValue) {
                    matrix[i][j] = clipValue;
                } else if (matrix[i][j] < -clipValue) {
                    matrix[i][j] = -clipValue;
                }
            }
        }
    }

    public Map<String, double[][]> getGradients() {
        return gradients;
    }

    public double[][] getGradient(String key) {
        return gradients.get(key);
    }

    public double[][] getWeightGradient(int layerIndex) {
        return gradients.get("dW" + layerIndex);
    }

    public double[][] getBiasGradient(int layerIndex) {
        return gradients.get("db" + layerIndex);
    }

    public Map<String, Double> checkGradients() {
        Map<String, Double> stats = new HashMap<>();

        for (Map.Entry<String, double[][]> entry : gradients.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith("dW") || key.startsWith("db")) {
                double[][] grad = entry.getValue();
                double sum = 0.0;
                double sumSquared = 0.0;
                double max = Double.NEGATIVE_INFINITY;
                double min = Double.POSITIVE_INFINITY;
                int count = 0;
                boolean hasNaN = false;
                boolean hasInf = false;

                for (int i = 0; i < grad.length; i++) {
                    for (int j = 0; j < grad[0].length; j++) {
                        double val = grad[i][j];
                        double absVal = Math.abs(val);

                        sum += absVal;
                        sumSquared += val * val;
                        count++;

                        if (absVal > max) max = absVal;
                        if (absVal < min) min = absVal;

                        if (Double.isNaN(val)) hasNaN = true;
                        if (Double.isInfinite(val)) hasInf = true;
                    }
                }

                double mean = sum / count;
                double variance = (sumSquared / count) - (mean * mean);
                double std = Math.sqrt(Math.abs(variance));

                stats.put(key + "_mean", mean);
                stats.put(key + "_std", std);
                stats.put(key + "_max", max);
                stats.put(key + "_min", min);

                if (hasNaN) stats.put(key + "_has_nan", 1.0);
                if (hasInf) stats.put(key + "_has_inf", 1.0);
                if (max > 100.0) stats.put(key + "_exploding", 1.0);
                if (max < 1e-7) stats.put(key + "_vanishing", 1.0);
            }
        }

        return stats;
    }

    public void printGradientStats() {
        Map<String, Double> stats = checkGradients();

        System.out.println("\n=== Gradient Statistics ===");
        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            System.out.printf("%-20s: %.6e\n", entry.getKey(), entry.getValue());
        }
        System.out.println("==========================\n");
    }

    private static class LinearGradients {
        double[][] dW;
        double[][] db;
        double[][] dA_prev;
    }
}