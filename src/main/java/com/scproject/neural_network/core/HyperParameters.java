package com.scproject.neural_network.core;

/**
 * Configuration class for neural network hyperparameters
 */
public class HyperParameters {
    private double learningRate = 0.01;
    private int epochs = 100;
    private int batchSize = 32;
    private String initializer = "xavier";
    private String loss = "mse";
    private boolean shuffle = true;
    private Integer seed = null;
    private double l2Regularization = 0.0;
    
    // Default constructor
    public HyperParameters() {}
    
    // Builder-style constructor
    public HyperParameters(double learningRate, int epochs, int batchSize, 
                          String initializer, String loss, boolean shuffle, Integer seed) {
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.batchSize = batchSize;
        this.initializer = initializer;
        this.loss = loss;
        this.shuffle = shuffle;
        this.seed = seed;
        this.l2Regularization = 0.0;
        validate();
    }
    
    // Constructor with L2 regularization
    public HyperParameters(double learningRate, int epochs, int batchSize, 
                          String initializer, String loss, boolean shuffle, Integer seed, double l2Reg) {
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.batchSize = batchSize;
        this.initializer = initializer;
        this.loss = loss;
        this.shuffle = shuffle;
        this.seed = seed;
        this.l2Regularization = l2Reg;
        validate();
    }
    
    public void validate() {
        if (learningRate <= 0) {
            throw new IllegalArgumentException("Learning rate must be positive");
        }
        if (epochs <= 0) {
            throw new IllegalArgumentException("Epochs must be positive");
        }
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size must be positive");
        }
        if (!isValidInitializer(initializer)) {
            throw new IllegalArgumentException("Initializer must be one of: xavier, he, uniform");
        }
        if (!isValidLoss(loss)) {
            throw new IllegalArgumentException("Loss must be one of: mse, cross_entropy");
        }
    }
    
    private boolean isValidInitializer(String init) {
        return "xavier".equalsIgnoreCase(init) || 
               "he".equalsIgnoreCase(init) || 
               "uniform".equalsIgnoreCase(init);
    }
    
    private boolean isValidLoss(String lossName) {
        return "mse".equalsIgnoreCase(lossName) || 
               "cross_entropy".equalsIgnoreCase(lossName);
    }
    
    // Getters and setters
    public double getLearningRate() { return learningRate; }
    public void setLearningRate(double learningRate) { 
        this.learningRate = learningRate; 
        validate();
    }
    
    public int getEpochs() { return epochs; }
    public void setEpochs(int epochs) { 
        this.epochs = epochs; 
        validate();
    }
    
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { 
        this.batchSize = batchSize; 
        validate();
    }
    
    public String getInitializer() { return initializer; }
    public void setInitializer(String initializer) { 
        this.initializer = initializer; 
        validate();
    }
    
    public String getLoss() { return loss; }
    public void setLoss(String loss) { 
        this.loss = loss; 
        validate();
    }
    
    public boolean isShuffle() { return shuffle; }
    public void setShuffle(boolean shuffle) { this.shuffle = shuffle; }
    
    public Integer getSeed() { return seed; }
    public void setSeed(Integer seed) { this.seed = seed; }
    
    public double getL2Regularization() { return l2Regularization; }
    public void setL2Regularization(double l2Regularization) { this.l2Regularization = l2Regularization; }
    
    @Override
    public String toString() {
        return String.format("HyperParameters{lr=%.3f, epochs=%d, batchSize=%d, " +
                           "initializer='%s', loss='%s', shuffle=%b, seed=%s}",
                           learningRate, epochs, batchSize, initializer, loss, shuffle, seed);
    }
}