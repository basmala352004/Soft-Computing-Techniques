package com.scproject.neural_network.demo;

import com.scproject.neural_network.core.*;
import com.scproject.neural_network.utils.DataUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Heart Disease Prediction using the new modular structure
 */
public class HeartDiseaseDemo {
    
    private static final String DATA_PATH = "src/main/java/com/scproject/neural_network/data/heart+disease/processed.cleveland.data";
    private static final String[] CLASS_NAMES = {"No Disease", "Disease Present"};
    
    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("HEART DISEASE PREDICTION - MODULAR NEURAL NETWORK");
        System.out.println("======================================================================");
        
        try {
            // Load and prepare data
            DataResult data = loadAndPreprocessData();
            printDatasetInfo(data);
            
            // Configure hyperparameters
            HyperParameters params = new HyperParameters(
                0.005,   // learning rate
                200,     // epochs
                32,      // batch size
                "xavier", // initializer
                "cross_entropy", // loss
                true,    // shuffle
                42,      // seed
                0.01     // L2 regularization
            );
            
            // Build model with new modular structure
            Network model = new Network(data.XTrain[0].length, params);
            model.addLayer(16, "relu", 0.3);    // 30% dropout
            model.addLayer(8, "relu", 0.2);     // 20% dropout
            model.addLayer(2, "linear");         // No dropout on output
            
            printModelSummary(model);
            
            // Train model
            System.out.println("\n======================================================================");
            System.out.println("TRAINING");
            System.out.println("======================================================================");
            
            Map<String, List<Double>> history = model.fit(
                data.XTrain, data.yTrain, data.XVal, data.yVal, true
            );
            
            // Evaluate model
            System.out.println("\n======================================================================");
            System.out.println("EVALUATION RESULTS");
            System.out.println("======================================================================");
            
            EvaluationResult trainResult = model.evaluate(data.XTrain, data.yTrain);
            EvaluationResult valResult = model.evaluate(data.XVal, data.yVal);
            EvaluationResult testResult = model.evaluate(data.XTest, data.yTest);
            
            System.out.printf("\nTraining Set:\n");
            System.out.printf("  Loss: %.4f\n", trainResult.getLoss());
            System.out.printf("  Accuracy: %.4f\n", trainResult.getAccuracy());
            
            System.out.printf("\nValidation Set:\n");
            System.out.printf("  Loss: %.4f\n", valResult.getLoss());
            System.out.printf("  Accuracy: %.4f\n", valResult.getAccuracy());
            
            System.out.printf("\nTest Set:\n");
            System.out.printf("  Loss: %.4f\n", testResult.getLoss());
            System.out.printf("  Accuracy: %.4f\n", testResult.getAccuracy());
            
            // Sample predictions
            printSamplePredictions(model, data.XTest, data.yTest);
            
            System.out.println("\n======================================================================");
            System.out.println("MODULAR NEURAL NETWORK DEMO COMPLETE");
            System.out.println("======================================================================");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static class DataResult {
        final double[][] XTrain, XVal, XTest;
        final double[][] yTrain, yVal, yTest;
        
        DataResult(double[][] XTrain, double[][] XVal, double[][] XTest,
                  double[][] yTrain, double[][] yVal, double[][] yTest) {
            this.XTrain = XTrain;
            this.XVal = XVal;
            this.XTest = XTest;
            this.yTrain = yTrain;
            this.yVal = yVal;
            this.yTest = yTest;
        }
    }
    
    private static DataResult loadAndPreprocessData() throws IOException {
        // Load heart disease data
        List<double[]> dataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(DATA_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                double[] row = new double[parts.length];
                
                for (int i = 0; i < parts.length; i++) {
                    if ("?".equals(parts[i].trim())) {
                        row[i] = Double.NaN;
                    } else {
                        row[i] = Double.parseDouble(parts[i].trim());
                    }
                }
                dataList.add(row);
            }
        }
        
        // Convert to arrays
        double[][] data = dataList.toArray(new double[0][]);
        data = DataUtils.imputeMissingValues(data, Double.NaN);
        
        // Separate features and labels
        double[][] X = new double[data.length][data[0].length - 1];
        int[] yInt = new int[data.length];
        
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, X[i], 0, data[0].length - 1);
            yInt[i] = data[i][data[0].length - 1] > 0 ? 1 : 0;
        }
        
        double[][] y = DataUtils.toOneHot(yInt, 2);
        
        // Create splits
        DataUtils.TrainTestSplit firstSplit = DataUtils.trainTestSplit(X, y, 0.2, true, 42);
        DataUtils.TrainTestSplit secondSplit = DataUtils.trainTestSplit(
            firstSplit.XTrain, firstSplit.yTrain, 0.125, true, 42);
        
        // Standardize features
        DataUtils.StandardizationResult stdResult = DataUtils.standardize(secondSplit.XTrain);
        double[][] XTrainStd = stdResult.standardized;
        double[][] XValStd = DataUtils.applyStandardization(secondSplit.XTest, stdResult.mean, stdResult.std);
        double[][] XTestStd = DataUtils.applyStandardization(firstSplit.XTest, stdResult.mean, stdResult.std);
        
        return new DataResult(XTrainStd, XValStd, XTestStd, 
                             secondSplit.yTrain, secondSplit.yTest, firstSplit.yTest);
    }
    
    private static void printDatasetInfo(DataResult data) {
        System.out.println("\n======================================================================");
        System.out.println("DATASET INFORMATION");
        System.out.println("======================================================================");
        
        int totalSamples = data.XTrain.length + data.XVal.length + data.XTest.length;
        System.out.printf("Total samples: %d\n", totalSamples);
        System.out.printf("Number of features: %d\n", data.XTrain[0].length);
        
        System.out.printf("\nData splits:\n");
        System.out.printf("  - Training: %d samples\n", data.XTrain.length);
        System.out.printf("  - Validation: %d samples\n", data.XVal.length);
        System.out.printf("  - Test: %d samples\n", data.XTest.length);
    }
    
    private static void printModelSummary(Network model) {
        System.out.println("\n======================================================================");
        System.out.println("MODEL ARCHITECTURE (MODULAR STRUCTURE)");
        System.out.println("======================================================================");
        
        System.out.printf("Input size: %d\n", model.getInputSize());
        
        int totalParams = 0;
        for (int i = 0; i < model.getLayers().size(); i++) {
            Layer layer = model.getLayers().get(i);
            int params = layer.getParameterCount();
            totalParams += params;
            
            System.out.printf("Layer %d: %d -> %d (params: %d)\n",
                             i + 1, layer.getInputSize(), layer.getOutputSize(), params);
        }
        
        System.out.printf("\nTotal parameters: %,d\n", totalParams);
        System.out.printf("Loss function: %s\n", model.getHyperParams().getLoss());
        System.out.printf("Learning rate: %.4f\n", model.getHyperParams().getLearningRate());
        System.out.printf("L2 Regularization: %.4f\n", model.getHyperParams().getL2Regularization());
    }
    
    private static void printSamplePredictions(Network model, double[][] XTest, double[][] yTest) {
        System.out.println("\n======================================================================");
        System.out.println("SAMPLE PREDICTIONS");
        System.out.println("======================================================================");
        
        Random random = new Random(42);
        int[] sampleIndices = random.ints(5, 0, XTest.length).toArray();
        
        for (int idx : sampleIndices) {
            double[][] sampleX = {XTest[idx]};
            double[][] predLogits = model.predict(sampleX);
            
            // Apply softmax to get probabilities
            double[] probs = softmax(predLogits[0]);
            
            int predLabel = probs[0] > probs[1] ? 0 : 1;
            int trueLabel = DataUtils.fromOneHot(new double[][]{yTest[idx]})[0];
            double confidence = probs[predLabel];
            
            System.out.printf("\nSample %d:\n", idx);
            System.out.printf("  True: %s\n", CLASS_NAMES[trueLabel]);
            System.out.printf("  Predicted: %s (confidence: %.2f%%)\n", 
                             CLASS_NAMES[predLabel], confidence * 100);
            System.out.printf("  Probabilities: No Disease=%.2f%%, Disease=%.2f%%\n",
                             probs[0] * 100, probs[1] * 100);
        }
    }
    
    private static double[] softmax(double[] logits) {
        double max = Arrays.stream(logits).max().orElse(0.0);
        double[] exp = new double[logits.length];
        double sum = 0.0;
        
        for (int i = 0; i < logits.length; i++) {
            exp[i] = Math.exp(logits[i] - max);
            sum += exp[i];
        }
        
        for (int i = 0; i < exp.length; i++) {
            exp[i] /= sum;
        }
        
        return exp;
    }
}