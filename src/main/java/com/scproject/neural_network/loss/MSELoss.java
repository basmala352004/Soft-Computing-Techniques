package com.scproject.neural_network.loss;

public class MSELoss implements LossFunction {
    @Override
    public double forward(double[][] predictions, double[][] targets) {
        int m = predictions.length;
        int n = predictions[0].length;
        double sum = 0.0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double diff = predictions[i][j] - targets[i][j];
                sum += diff * diff;
            }
        }

        return sum / (m * n);
    }

    @Override
    public double[][] backward(double[][] predictions, double[][] targets) {
        int m = predictions.length;
        int n = predictions[0].length;
        double[][] gradient = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                gradient[i][j] = 2.0 * (predictions[i][j] - targets[i][j]) / (m * n);
            }
        }

        return gradient;
    }

    @Override
    public String getName() {
        return "MSE";
    }
}
