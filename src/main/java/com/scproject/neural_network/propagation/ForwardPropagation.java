package com.scproject.neural_network.propagation;

import com.scproject.neural_network.core.Layer;
import com.scproject.neural_network.activations.Activation;
import java.util.List;
import java.util.ArrayList;

public class ForwardPropagation {

    private PropagationCache cache;
    private List<double[][]> layerOutputs;

    public ForwardPropagation() {
        this.cache = new PropagationCache();
        this.layerOutputs = new ArrayList<>();
    }

    public double[][] forwardPass(double[][] X, List<Layer> layers, boolean training) {
        if (X == null || X.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty");
        }

        if (training) {
            cache.clear();
            cache.storeInput(X);
        }
        layerOutputs.clear();
        double[][] currentInput = X;

        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            double[][] Z = linearForward(currentInput, layer.getWeights(), layer.getBias());
            double[][] A = activationForward(Z, layer.getActivation());

            if (training) {
                cache.storeLayer(i, currentInput, Z,
                        layer.getWeights(), layer.getBias(),
                        layer.getActivation());
            }

            layerOutputs.add(A);
            currentInput = A;
        }

        return currentInput;
    }

    private double[][] linearForward(double[][] A_prev, double[][] W, double[][] b) {
        int m = A_prev.length;
        int n = W[0].length;
        double[][] Z = matrixMultiply(A_prev, W);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Z[i][j] += b[0][j];
            }
        }

        return Z;
    }

    private double[][] activationForward(double[][] Z, Activation activation) {
        int rows = Z.length;
        int cols = Z[0].length;
        double[][] A = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                A[i][j] = activation.forward(Z[i][j]);
            }
        }

        return A;
    }

    public double[][] predict(double[][] X, List<Layer> layers) {
        return forwardPass(X, layers, false);
    }

    public double[] predictSingle(double[] x, List<Layer> layers) {
        // Convert 1D to 2D
        double[][] X = new double[1][x.length];
        X[0] = x;

        double[][] result = predict(X, layers);
        return result[0];
    }

    private double[][] matrixMultiply(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        int p = B[0].length;

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

    public PropagationCache getCache() {
        return cache;
    }

    public List<double[][]> getLayerOutputs() {
        return layerOutputs;
    }

    public double[][] getLayerOutput(int layerIndex) {
        if (layerIndex >= 0 && layerIndex < layerOutputs.size()) {
            return layerOutputs.get(layerIndex);
        }
        throw new IndexOutOfBoundsException("Layer index out of bounds: " + layerIndex);
    }
}