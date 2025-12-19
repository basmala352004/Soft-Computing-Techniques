package com.scproject.neural_network.core;

import com.scproject.neural_network.initializers.WeightInitializer;
import com.scproject.neural_network.loss.LossFunction;
import com.scproject.neural_network.propagation.BackwardPropagation;
import com.scproject.neural_network.propagation.ForwardPropagation;

import java.util.*;

/**
 * Main Neural Network class implementing feedforward architecture
 */
public class Network {
    private final int inputSize;
    private final List<Layer> layers;
    private final HyperParameters hyperParams;
    private LossFunction lossFunction;
    private final Map<String, List<Double>> history;
    
    public Network(int inputSize, HyperParameters hyperParams) {
        if (inputSize <= 0) {
            throw new IllegalArgumentException("Input size must be positive");
        }
        this.inputSize = inputSize;
        this.layers = new ArrayList<>();
        this.hyperParams = hyperParams != null ? hyperParams : new HyperParameters();
        this.history = new HashMap<>();
        this.history.put("loss", new ArrayList<>());
        this.history.put("accuracy", new ArrayList<>());
        
        // Initialize loss function
        this.lossFunction = LossFunction.create(this.hyperParams.getLoss());
        
        // Set random seed for reproducibility
        if (this.hyperParams.getSeed() != null) {
            WeightInitializer.setSeed(this.hyperParams.getSeed());
        }
    }
    
    /**
     * Add a dense layer to the network
     */
    public void addLayer(int outputSize, String activation, String initializer, double dropoutRate) {
        int prevSize = layers.isEmpty() ? inputSize : layers.get(layers.size() - 1).getOutputSize();
        String init = initializer != null ? initializer : hyperParams.getInitializer();
        
        DenseLayer layer = new DenseLayer(prevSize, outputSize, activation, init, dropoutRate);
        layers.add(layer);
    }
    
    public void addLayer(int outputSize, String activation, double dropoutRate) {
        addLayer(outputSize, activation, null, dropoutRate);
    }
    
    public void addLayer(int outputSize, String activation) {
        addLayer(outputSize, activation, null, 0.0);
    }
    
    /**
     * Forward propagation through all layers
     */
    public double[][] forward(double[][] X) {
        return ForwardPropagation.forward(X, layers);
    }
    
    /**
     * Forward propagation with training mode control
     */
    public double[][] forward(double[][] X, boolean training) {
        return ForwardPropagation.forward(X, layers, training);
    }
    
    /**
     * Train the neural network with early stopping
     */
    public Map<String, List<Double>> fit(double[][] X, double[][] y, 
                                        double[][] XVal, double[][] yVal, 
                                        boolean verbose) {
        if (X.length != y.length) {
            throw new IllegalArgumentException("X and y must have same number of samples");
        }
        if (layers.isEmpty()) {
            throw new IllegalStateException("Add at least one layer before training");
        }
        
        // Add validation history if validation data provided
        if (XVal != null && yVal != null) {
            history.put("val_loss", new ArrayList<>());
            history.put("val_accuracy", new ArrayList<>());
        }
        
        // Early stopping parameters
        double bestValLoss = Double.MAX_VALUE;
        int patienceCounter = 0;
        int patience = 20;
        
        for (int epoch = 0; epoch < hyperParams.getEpochs(); epoch++) {
            List<Double> epochLosses = new ArrayList<>();
            
            // Set training mode
            setTraining(true);
            
            // Mini-batch training
            List<int[]> batches = createBatches(X.length, hyperParams.getBatchSize(), 
                                              hyperParams.isShuffle());
            
            for (int[] batch : batches) {
                double[][] XBatch = getBatch(X, batch);
                double[][] yBatch = getBatch(y, batch);
                
                // Forward pass in training mode
                double[][] predictions = forward(XBatch, true);
                
                // Compute loss
                double loss = lossFunction.forward(predictions, yBatch);
                epochLosses.add(loss);
                
                // Backward pass
                double[][] lossGrad = lossFunction.backward();
                BackwardPropagation.backward(lossGrad, layers, hyperParams.getLearningRate(), 
                                           hyperParams.getL2Regularization());
            }
            
            // Record training metrics
            double meanLoss = epochLosses.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            history.get("loss").add(meanLoss);
            
            // Evaluate in inference mode (no dropout)
            setTraining(false);
            double trainAcc = calculateAccuracy(forward(X), y);
            history.get("accuracy").add(trainAcc);
            
            // Validation metrics and early stopping
            if (XVal != null && yVal != null) {
                EvaluationResult valResult = evaluate(XVal, yVal);
                double valLoss = valResult.getLoss();
                history.get("val_loss").add(valLoss);
                history.get("val_accuracy").add(valResult.getAccuracy());
                
                // Early stopping check
                if (valLoss < bestValLoss) {
                    bestValLoss = valLoss;
                    patienceCounter = 0;
                } else {
                    patienceCounter++;
                    if (patienceCounter >= patience) {
                        if (verbose) {
                            System.out.println(String.format("Early stopping at epoch %d (best val_loss: %.4f)", 
                                                            epoch + 1, bestValLoss));
                        }
                        break;
                    }
                }
            }
            
            // Print progress
            if (verbose && (epoch + 1) % Math.max(1, hyperParams.getEpochs() / 10) == 0) {
                String msg = String.format("Epoch %d/%d - loss: %.4f, accuracy: %.4f", 
                                         epoch + 1, hyperParams.getEpochs(), meanLoss, trainAcc);
                if (XVal != null) {
                    double valLoss = history.get("val_loss").get(history.get("val_loss").size() - 1);
                    double valAcc = history.get("val_accuracy").get(history.get("val_accuracy").size() - 1);
                    msg += String.format(", val_loss: %.4f, val_accuracy: %.4f", valLoss, valAcc);
                }
                System.out.println(msg);
            }
        }
        
        return history;
    }
    
    public Map<String, List<Double>> fit(double[][] X, double[][] y) {
        return fit(X, y, null, null, true);
    }
    
    /**
     * Make predictions
     */
    public double[][] predict(double[][] X, boolean asLabels) {
        setTraining(false); // Inference mode
        double[][] predictions = forward(X);
        
        if (asLabels) {
            return convertToLabels(predictions);
        }
        return predictions;
    }
    
    public double[][] predict(double[][] X) {
        return predict(X, false);
    }
    
    /**
     * Evaluate the model
     */
    public EvaluationResult evaluate(double[][] X, double[][] y) {
        setTraining(false); // Inference mode
        double[][] predictions = forward(X);
        double loss = lossFunction.forward(predictions, y);
        double accuracy = calculateAccuracy(predictions, y);
        
        return new EvaluationResult(loss, accuracy);
    }
    
    /**
     * Set training mode for all layers (affects dropout)
     */
    public void setTraining(boolean training) {
        for (Layer layer : layers) {
            layer.setTraining(training);
        }
    }
    
    // Helper methods
    private double calculateAccuracy(double[][] predictions, double[][] y) {
        if (predictions.length != y.length) return 0.0;
        
        int correct = 0;
        for (int i = 0; i < predictions.length; i++) {
            int predLabel = argmax(predictions[i]);
            int trueLabel = argmax(y[i]);
            if (predLabel == trueLabel) correct++;
        }
        
        return (double) correct / predictions.length;
    }
    
    private double[][] convertToLabels(double[][] predictions) {
        double[][] labels = new double[predictions.length][predictions[0].length];
        for (int i = 0; i < predictions.length; i++) {
            int maxIdx = argmax(predictions[i]);
            labels[i][maxIdx] = 1.0;
        }
        return labels;
    }
    
    private int argmax(double[] array) {
        int maxIdx = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIdx]) {
                maxIdx = i;
            }
        }
        return maxIdx;
    }
    
    private List<int[]> createBatches(int numSamples, int batchSize, boolean shuffle) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numSamples; i++) {
            indices.add(i);
        }
        
        if (shuffle) {
            Collections.shuffle(indices);
        }
        
        List<int[]> batches = new ArrayList<>();
        for (int i = 0; i < numSamples; i += batchSize) {
            int end = Math.min(i + batchSize, numSamples);
            int[] batch = new int[end - i];
            for (int j = 0; j < batch.length; j++) {
                batch[j] = indices.get(i + j);
            }
            batches.add(batch);
        }
        
        return batches;
    }
    
    private double[][] getBatch(double[][] data, int[] indices) {
        double[][] batch = new double[indices.length][data[0].length];
        for (int i = 0; i < indices.length; i++) {
            System.arraycopy(data[indices[i]], 0, batch[i], 0, data[0].length);
        }
        return batch;
    }
    
    // Getters
    public int getInputSize() { return inputSize; }
    public List<Layer> getLayers() { return layers; }
    public HyperParameters getHyperParams() { return hyperParams; }
    public Map<String, List<Double>> getHistory() { return history; }
}