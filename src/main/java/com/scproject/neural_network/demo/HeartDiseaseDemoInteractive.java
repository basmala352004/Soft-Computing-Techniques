package com.scproject.neural_network.demo;

import com.scproject.neural_network.core.*;
import com.scproject.neural_network.utils.DataUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Heart Disease Prediction – Interactive Scanner Input
 * Press ENTER to use default values.
 */
public class HeartDiseaseDemoInteractive {

    /* ===================== DEFAULTS ===================== */
    private static final String DEFAULT_DATA_PATH =
            "src/main/java/com/scproject/neural_network/data/heart+disease/processed.cleveland.data";
    private static final double DEFAULT_LEARNING_RATE = 0.005;
    private static final int DEFAULT_EPOCHS = 200;
    private static final int DEFAULT_BATCH_SIZE = 32;
    private static final String DEFAULT_INITIALIZER = "xavier";
    private static final String DEFAULT_LOSS = "cross_entropy";
    private static final boolean DEFAULT_SHUFFLE = true;
    private static final int DEFAULT_SEED = 42;
    private static final double DEFAULT_L2 = 0.01;

    private static final String[] CLASS_NAMES =
            {"No Disease", "Disease Present"};

    /* ===================== MAIN ===================== */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Config config = readConfig(scanner);

        System.out.println("\n======================================================================");
        System.out.println("HEART DISEASE PREDICTION - INTERACTIVE MODE");
        System.out.println("======================================================================");

        try {
            DataResult data = loadAndPreprocessData(config.dataPath);
            printDatasetInfo(data);

            HyperParameters params = new HyperParameters(
                    config.learningRate,
                    config.epochs,
                    config.batchSize,
                    config.initializer,
                    config.loss,
                    config.shuffle,
                    config.seed,
                    config.l2
            );

            Network model = new Network(data.XTrain[0].length, params);
            model.addLayer(16, "relu", 0.3);
            model.addLayer(8, "relu", 0.2);
            model.addLayer(2, "linear");

            printModelSummary(model);

            System.out.println("\n======================================================================");
            System.out.println("TRAINING");
            System.out.println("======================================================================");

            model.fit(data.XTrain, data.yTrain, data.XVal, data.yVal, true);

            System.out.println("\n======================================================================");
            System.out.println("EVALUATION RESULTS");
            System.out.println("======================================================================");

            evaluate(model, data);
            printSamplePredictions(model, data.XTest, data.yTest);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /* ===================== INTERACTIVE INPUT ===================== */
    private static Config readConfig(Scanner sc) {

        System.out.println("Press ENTER to accept default values\n");

        String dataPath = prompt(
                sc, "Dataset path", DEFAULT_DATA_PATH);

        double lr = promptDouble(
                sc, "Learning rate", DEFAULT_LEARNING_RATE);

        int epochs = promptInt(
                sc, "Epochs", DEFAULT_EPOCHS);

        int batch = promptInt(
                sc, "Batch size", DEFAULT_BATCH_SIZE);

        String init = prompt(
                sc, "Weight initializer", DEFAULT_INITIALIZER);

        String loss = prompt(
                sc, "Loss function", DEFAULT_LOSS);

        boolean shuffle = promptBoolean(
                sc, "Shuffle data", DEFAULT_SHUFFLE);

        int seed = promptInt(
                sc, "Random seed", DEFAULT_SEED);

        double l2 = promptDouble(
                sc, "L2 regularization", DEFAULT_L2);

        return new Config(
                dataPath, lr, epochs, batch,
                init, loss, shuffle, seed, l2
        );
    }

    private static String prompt(Scanner sc, String label, String def) {
        System.out.printf("%s [%s]: ", label, def);
        String input = sc.nextLine().trim();
        return input.isEmpty() ? def : input;
    }

    private static int promptInt(Scanner sc, String label, int def) {
        while (true) {
            try {
                String input = prompt(sc, label, String.valueOf(def));
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    private static double promptDouble(Scanner sc, String label, double def) {
        while (true) {
            try {
                String input = prompt(sc, label, String.valueOf(def));
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static boolean promptBoolean(Scanner sc, String label, boolean def) {
        String defStr = def ? "true" : "false";
        while (true) {
            String input = prompt(sc, label + " (true/false)", defStr)
                    .toLowerCase();
            if ("true".equals(input) || "false".equals(input)) {
                return Boolean.parseBoolean(input);
            }
            System.out.println("Please enter true or false.");
        }
    }

    /* ===================== CONFIG ===================== */
    private static class Config {
        final String dataPath;
        final double learningRate;
        final int epochs;
        final int batchSize;
        final String initializer;
        final String loss;
        final boolean shuffle;
        final int seed;
        final double l2;

        Config(String dataPath, double learningRate, int epochs, int batchSize,
               String initializer, String loss, boolean shuffle,
               int seed, double l2) {

            this.dataPath = dataPath;
            this.learningRate = learningRate;
            this.epochs = epochs;
            this.batchSize = batchSize;
            this.initializer = initializer;
            this.loss = loss;
            this.shuffle = shuffle;
            this.seed = seed;
            this.l2 = l2;
        }
    }

    /* ===================== DATA ===================== */
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

    private static DataResult loadAndPreprocessData(String path)
            throws IOException {

        List<double[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                double[] r = new double[p.length];
                for (int i = 0; i < p.length; i++) {
                    r[i] = "?".equals(p[i].trim())
                            ? Double.NaN
                            : Double.parseDouble(p[i].trim());
                }
                rows.add(r);
            }
        }

        double[][] data = DataUtils.imputeMissingValues(
                rows.toArray(new double[0][]), Double.NaN);

        double[][] X = new double[data.length][data[0].length - 1];
        int[] yInt = new int[data.length];

        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, X[i], 0, X[i].length);
            yInt[i] = data[i][data[0].length - 1] > 0 ? 1 : 0;
        }

        double[][] y = DataUtils.toOneHot(yInt, 2);

        DataUtils.TrainTestSplit s1 =
                DataUtils.trainTestSplit(X, y, 0.2, true, DEFAULT_SEED);

        DataUtils.TrainTestSplit s2 =
                DataUtils.trainTestSplit(
                        s1.XTrain, s1.yTrain, 0.125, true, DEFAULT_SEED);

        DataUtils.StandardizationResult std =
                DataUtils.standardize(s2.XTrain);

        return new DataResult(
                std.standardized,
                DataUtils.applyStandardization(
                        s2.XTest, std.mean, std.std),
                DataUtils.applyStandardization(
                        s1.XTest, std.mean, std.std),
                s2.yTrain,
                s2.yTest,
                s1.yTest
        );
    }

    /* ===================== OUTPUT ===================== */
    private static void printDatasetInfo(DataResult data) {
        System.out.printf(
                "\nTrain: %d | Val: %d | Test: %d\n",
                data.XTrain.length,
                data.XVal.length,
                data.XTest.length
        );
    }

    private static void printModelSummary(Network model) {
        System.out.printf("\nInput size: %d\n", model.getInputSize());
        for (int i = 0; i < model.getLayers().size(); i++) {
            Layer l = model.getLayers().get(i);
            System.out.printf("Layer %d: %d → %d\n",
                    i + 1, l.getInputSize(), l.getOutputSize());
        }
    }

    private static void evaluate(Network model, DataResult data) {
        EvaluationResult tr = model.evaluate(data.XTrain, data.yTrain);
        EvaluationResult va = model.evaluate(data.XVal, data.yVal);
        EvaluationResult te = model.evaluate(data.XTest, data.yTest);

        System.out.printf("\nTrain  Acc: %.4f\n", tr.getAccuracy());
        System.out.printf("Val    Acc: %.4f\n", va.getAccuracy());
        System.out.printf("Test   Acc: %.4f\n", te.getAccuracy());
    }

    private static void printSamplePredictions(
            Network model, double[][] XTest, double[][] yTest) {

        Random r = new Random(DEFAULT_SEED);
        for (int i : r.ints(5, 0, XTest.length).toArray()) {

            double[] probs = softmax(model.predict(
                    new double[][]{XTest[i]})[0]);

            int pred = probs[0] > probs[1] ? 0 : 1;
            int truth = DataUtils.fromOneHot(
                    new double[][]{yTest[i]})[0];

            System.out.printf(
                    "True: %-10s | Pred: %-10s | Conf: %.2f%%\n",
                    CLASS_NAMES[truth],
                    CLASS_NAMES[pred],
                    probs[pred] * 100
            );
        }
    }

    private static double[] softmax(double[] logits) {
        double max = Arrays.stream(logits).max().orElse(0);
        double sum = 0;
        double[] exp = new double[logits.length];

        for (int i = 0; i < logits.length; i++) {
            exp[i] = Math.exp(logits[i] - max);
            sum += exp[i];
        }
        for (int i = 0; i < exp.length; i++) exp[i] /= sum;
        return exp;
    }
}
