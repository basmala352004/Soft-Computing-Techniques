package com.scproject.neural_network.core;

/**
 * Matrix operations utility class
 */
public class MatrixUtils {
    
    /**
     * Matrix multiplication: A * B
     */
    public static double[][] multiply(double[][] A, double[][] B) {
        if (A[0].length != B.length) {
            throw new IllegalArgumentException("Matrix dimensions don't match for multiplication");
        }
        
        double[][] result = new double[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }
    
    /**
     * Matrix addition: A + B
     */
    public static double[][] add(double[][] A, double[][] B) {
        if (A.length != B.length || A[0].length != B[0].length) {
            throw new IllegalArgumentException("Matrix dimensions don't match for addition");
        }
        
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }
    
    /**
     * Matrix addition with broadcast: A + b (where b is a vector)
     */
    public static double[][] add(double[][] A, double[] b) {
        if (A[0].length != b.length) {
            throw new IllegalArgumentException("Vector length doesn't match matrix columns");
        }
        
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] + b[j];
            }
        }
        return result;
    }
    
    /**
     * Matrix subtraction: A - B
     */
    public static double[][] subtract(double[][] A, double[][] B) {
        if (A.length != B.length || A[0].length != B[0].length) {
            throw new IllegalArgumentException("Matrix dimensions don't match for subtraction");
        }
        
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }
    
    /**
     * Vector subtraction: a - b
     */
    public static double[] subtract(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector lengths don't match");
        }
        
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }
    
    /**
     * Scalar multiplication: A * scalar
     */
    public static double[][] multiply(double[][] A, double scalar) {
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * scalar;
            }
        }
        return result;
    }
    
    /**
     * Vector scalar multiplication: v * scalar
     */
    public static double[] multiply(double[] v, double scalar) {
        double[] result = new double[v.length];
        for (int i = 0; i < v.length; i++) {
            result[i] = v[i] * scalar;
        }
        return result;
    }
    
    /**
     * Element-wise multiplication: A .* B
     */
    public static double[][] elementWiseMultiply(double[][] A, double[][] B) {
        if (A.length != B.length || A[0].length != B[0].length) {
            throw new IllegalArgumentException("Matrix dimensions don't match");
        }
        
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[i][j] = A[i][j] * B[i][j];
            }
        }
        return result;
    }
    
    /**
     * Matrix division: A / scalar
     */
    public static double[][] divide(double[][] A, double scalar) {
        return multiply(A, 1.0 / scalar);
    }
    
    /**
     * Matrix transpose
     */
    public static double[][] transpose(double[][] A) {
        double[][] result = new double[A[0].length][A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                result[j][i] = A[i][j];
            }
        }
        return result;
    }
    
    /**
     * Deep copy of matrix
     */
    public static double[][] copy(double[][] A) {
        double[][] result = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            System.arraycopy(A[i], 0, result[i], 0, A[0].length);
        }
        return result;
    }
    
    /**
     * Mean along axis 0 (column-wise mean)
     */
    public static double[] mean(double[][] A, int axis) {
        if (axis == 0) {
            // Column-wise mean
            double[] result = new double[A[0].length];
            for (int j = 0; j < A[0].length; j++) {
                double sum = 0.0;
                for (int i = 0; i < A.length; i++) {
                    sum += A[i][j];
                }
                result[j] = sum / A.length;
            }
            return result;
        } else {
            throw new IllegalArgumentException("Only axis=0 supported");
        }
    }
    
    /**
     * Print matrix for debugging
     */
    public static void print(double[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                System.out.printf("%.4f ", A[i][j]);
            }
            System.out.println();
        }
    }
}