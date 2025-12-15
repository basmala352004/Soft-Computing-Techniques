package com.scproject.fuzzy;

import com.scproject.fuzzy.core.*;
import com.scproject.fuzzy.inference.InferenceEngine;
import com.scproject.fuzzy.inference.MamdaniEngine;
import com.scproject.fuzzy.inference.SugenoEngine;
import com.scproject.fuzzy.membership.*;
import com.scproject.fuzzy.operator.*;
import com.scproject.fuzzy.rulebase.RuleBase;
import com.scproject.fuzzy.defuzzification.*;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static FuzzyVariable density;
    private static FuzzyVariable waitTime;
    private static FuzzyVariable duration;

    public static void main(String[] args) {
        printHeader();

        try {
            setupVariables();
            Map<String, Double> crispInputs = getUserInputs();
            InferenceEngine engine = selectInferenceEngine();
            FuzzyOperator andOp = selectAndOperator();
            FuzzyOperator orOp = selectOrOperator();
            DefuzzificationMethod defuzzMethod = selectDefuzzificationMethod();
            RuleBase ruleBase = createRulesInteractively();
            runFuzzyInference(crispInputs, engine, andOp, orOp, defuzzMethod, ruleBase);

        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void printHeader() {
        System.out.println("============================================================");
        System.out.println("     Interactive Fuzzy Logic System");
        System.out.println("     Traffic Light Timing Controller");
        System.out.println("============================================================");
    }

    private static void setupVariables() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STEP 1: Variable Definitions");
        System.out.println("=".repeat(60));

        density = new FuzzyVariable();
        density.setName("Density");
        density.setMinRange(0);
        density.setMaxRange(100);

        Map<String, FuzzySet> densitySets = new HashMap<>();
        densitySets.put("Low", new FuzzySet("Low", new TriangularMF(0, 0, 40)));
        densitySets.put("Medium", new FuzzySet("Medium", new TriangularMF(20, 50, 80)));
        densitySets.put("High", new FuzzySet("High", new TriangularMF(60, 100, 100)));
        density.setFuzzySets(densitySets);

        System.out.println("\nInput Variable 1: Traffic Density (0-100)");
        System.out.println("  Fuzzy Sets: Low, Medium, High");

        waitTime = new FuzzyVariable();
        waitTime.setName("WaitingTime");
        waitTime.setMinRange(0);
        waitTime.setMaxRange(120);

        Map<String, FuzzySet> waitSets = new HashMap<>();
        waitSets.put("Short", new FuzzySet("Short", new TriangularMF(0, 0, 40)));
        waitSets.put("Medium", new FuzzySet("Medium", new TriangularMF(30, 60, 90)));
        waitSets.put("Long", new FuzzySet("Long", new TriangularMF(80, 120, 120)));
        waitTime.setFuzzySets(waitSets);

        System.out.println("\nInput Variable 2: Waiting Time (0-120 seconds)");
        System.out.println("  Fuzzy Sets: Short, Medium, Long");

        duration = new FuzzyVariable();
        duration.setName("GreenDuration");
        duration.setMinRange(0);
        duration.setMaxRange(60);

        Map<String, FuzzySet> durationSets = new HashMap<>();
        durationSets.put("Short", new FuzzySet("Short", new TriangularMF(0, 0, 20)));
        durationSets.put("Medium", new FuzzySet("Medium", new TriangularMF(15, 30, 45)));
        durationSets.put("Long", new FuzzySet("Long", new TriangularMF(40, 60, 60)));
        duration.setFuzzySets(durationSets);

        System.out.println("\nOutput Variable: Green Light Duration (0-60 seconds)");
        System.out.println("  Fuzzy Sets: Short, Medium, Long");
    }

    private static Map<String, Double> getUserInputs() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STEP 2: Input Values");
        System.out.println("=".repeat(60));

        Map<String, Double> inputs = new HashMap<>();

        double densityValue = readDoubleInput("Enter Traffic Density (0-100): ", 0, 100);
        inputs.put("Density", densityValue);

        double waitValue = readDoubleInput("Enter Waiting Time (0-120 seconds): ", 0, 120);
        inputs.put("WaitingTime", waitValue);

        System.out.println("\nInputs recorded:");
        System.out.println("  Traffic Density: " + densityValue);
        System.out.println("  Waiting Time: " + waitValue + " seconds");

        return inputs;
    }

    private static InferenceEngine selectInferenceEngine() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STEP 3: Configure Inference Engine");
        System.out.println("=".repeat(60));

        System.out.println("\nAvailable Inference Engines:");
        System.out.println("1. Mamdani");
        System.out.println("2. Sugeno");

        int choice = readIntInput("Select engine (1 or 2): ", 1, 2);

        InferenceEngine engine;
        if (choice == 1) {
            engine = new MamdaniEngine();
            System.out.println("Using Mamdani Inference Engine");
        } else {
            engine = new SugenoEngine();
            System.out.println("Using Sugeno Inference Engine");
        }

        return engine;
    }

    private static FuzzyOperator selectAndOperator() {
        System.out.println("\nAvailable AND Operators:");
        System.out.println("1. MIN");
        System.out.println("2. PRODUCT");

        int choice = readIntInput("Select AND operator (1 or 2): ", 1, 2);

        FuzzyOperator op;
        if (choice == 1) {
            op = new MinOperator();
            System.out.println("Using MIN operator for AND");
        } else {
            op = new ProductOperator();
            System.out.println("Using PRODUCT operator for AND");
        }

        return op;
    }

    private static FuzzyOperator selectOrOperator() {
        System.out.println("\nAvailable OR Operators:");
        System.out.println("1. MAX");
        System.out.println("2. ALGEBRAIC SUM");

        int choice = readIntInput("Select OR operator (1 or 2): ", 1, 2);

        FuzzyOperator op;
        if (choice == 1) {
            op = new MaxOperator();
            System.out.println("Using MAX operator for OR");
        } else {
            op = new AlegbraicSumOperator();
            System.out.println("Using ALGEBRAIC SUM operator for OR");
        }

        return op;
    }

    private static DefuzzificationMethod selectDefuzzificationMethod() {
        System.out.println("\nAvailable Defuzzification Methods:");
        System.out.println("1. Centroid");
        System.out.println("2. Mean of Maximum");
        System.out.println("3. Maximum Membership");
        System.out.println("4. Weighted Average");

        int choice = readIntInput("Select defuzzification method (1-4): ", 1, 4);

        DefuzzificationMethod method;
        switch (choice) {
            case 1:
                method = new CentroidDefuzz();
                System.out.println("Using Centroid defuzzification");
                break;
            case 2:
                method = new MeanOfMaximumDefuzz();
                System.out.println("Using Mean of Maximum defuzzification");
                break;
            case 3:
                method = new MaxMembershipDefuzz();
                System.out.println("Using Maximum Membership defuzzification");
                break;
            default:
                method = new WeightedAverageDefuzz();
                System.out.println("Using Weighted Average defuzzification");
        }

        return method;
    }

    private static RuleBase createRulesInteractively() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STEP 4: Define Fuzzy Rules");
        System.out.println("=".repeat(60));

        RuleBase ruleBase = new RuleBase();

        System.out.println("\nAvailable Fuzzy Sets:");
        System.out.println("  Density: Low, Medium, High");
        System.out.println("  WaitingTime: Short, Medium, Long");
        System.out.println("  GreenDuration: Short, Medium, Long");

        int numRules = readIntInput("\nHow many rules do you want to create? ", 1, 20);

        for (int i = 1; i <= numRules; i++) {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("Rule " + i);
            System.out.println("-".repeat(60));

            FuzzyRule rule = createSingleRule();
            ruleBase.addRule(rule);

            System.out.println("Rule " + i + " added successfully");
        }

        System.out.println("\nTotal rules created: " + ruleBase.size());
        return ruleBase;
    }

    private static FuzzyRule createSingleRule() {
        Map<String, String> antecedents = new HashMap<>();

        System.out.println("\nHow many conditions for this rule?");
        System.out.println("1. One condition");
        System.out.println("2. Two conditions");

        int numConditions = readIntInput("Select (1 or 2): ", 1, 2);

        System.out.println("\nFirst Condition:");
        System.out.println("1. Density");
        System.out.println("2. WaitingTime");
        int var1 = readIntInput("Select variable (1 or 2): ", 1, 2);

        String variable1 = (var1 == 1) ? "Density" : "WaitingTime";
        String[] sets1 = (var1 == 1) ?
                new String[]{"Low", "Medium", "High"} :
                new String[]{"Short", "Medium", "Long"};

        System.out.println("\nSelect fuzzy set for " + variable1 + ":");
        for (int i = 0; i < sets1.length; i++) {
            System.out.println((i + 1) + ". " + sets1[i]);
        }
        int set1Idx = readIntInput("Select (1-" + sets1.length + "): ", 1, sets1.length);
        antecedents.put(variable1, sets1[set1Idx - 1]);

        String operator = "AND";
        if (numConditions == 2) {
            System.out.println("\nSelect logical operator:");
            System.out.println("1. AND");
            System.out.println("2. OR");
            int opChoice = readIntInput("Select (1 or 2): ", 1, 2);
            operator = (opChoice == 1) ? "AND" : "OR";

            System.out.println("\nSecond Condition:");
            System.out.println("1. Density");
            System.out.println("2. WaitingTime");
            int var2 = readIntInput("Select variable (1 or 2): ", 1, 2);

            String variable2 = (var2 == 1) ? "Density" : "WaitingTime";
            String[] sets2 = (var2 == 1) ?
                    new String[]{"Low", "Medium", "High"} :
                    new String[]{"Short", "Medium", "Long"};

            System.out.println("\nSelect fuzzy set for " + variable2 + ":");
            for (int i = 0; i < sets2.length; i++) {
                System.out.println((i + 1) + ". " + sets2[i]);
            }
            int set2Idx = readIntInput("Select (1-" + sets2.length + "): ", 1, sets2.length);
            antecedents.put(variable2, sets2[set2Idx - 1]);
        }

        System.out.println("\nTHEN (Output):");
        System.out.println("GreenDuration should be:");
        System.out.println("1. Short");
        System.out.println("2. Medium");
        System.out.println("3. Long");
        int outputChoice = readIntInput("Select (1-3): ", 1, 3);
        String[] outputs = {"Short", "Medium", "Long"};
        String consequent = outputs[outputChoice - 1];

        double weight = readDoubleInput("\nEnter rule weight (0.0 to 1.0): ", 0.0, 1.0);

        System.out.print("Enable this rule? (yes/no): ");
        String enabledStr = scanner.nextLine().trim().toLowerCase();
        boolean enabled = enabledStr.equals("yes") || enabledStr.equals("y");

        System.out.println("\nRule Summary:");
        System.out.println("  IF " + formatAntecedents(antecedents, operator));
        System.out.println("  THEN GreenDuration is " + consequent);
        System.out.println("  Weight: " + weight);
        System.out.println("  Status: " + (enabled ? "Enabled" : "Disabled"));

        return new FuzzyRule(antecedents, "GreenDuration", consequent, operator, weight, enabled);
    }

    private static String formatAntecedents(Map<String, String> antecedents, String operator) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, String> entry : antecedents.entrySet()) {
            if (count++ > 0) {
                sb.append(" ").append(operator).append(" ");
            }
            sb.append(entry.getKey()).append(" is ").append(entry.getValue());
        }
        return sb.toString();
    }

    private static void runFuzzyInference(
            Map<String, Double> crispInputs,
            InferenceEngine engine,
            FuzzyOperator andOp,
            FuzzyOperator orOp,
            DefuzzificationMethod defuzzMethod,
            RuleBase ruleBase
    ) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STEP 5: Fuzzy Inference Process");
        System.out.println("=".repeat(60));

        System.out.println("\nPhase 1: FUZZIFICATION");
        Map<String, Map<String, Double>> fuzzified = new HashMap<>();
        fuzzified.put("Density", density.fuzzify(crispInputs.get("Density")));
        fuzzified.put("WaitingTime", waitTime.fuzzify(crispInputs.get("WaitingTime")));

        System.out.println("Input: Density = " + crispInputs.get("Density"));
        System.out.println("Memberships: " + formatMemberships(fuzzified.get("Density")));

        System.out.println("\nInput: WaitingTime = " + crispInputs.get("WaitingTime"));
        System.out.println("Memberships: " + formatMemberships(fuzzified.get("WaitingTime")));

        System.out.println("\nPhase 2: RULE EVALUATION");
        Map<FuzzyRule, Double> ruleStrengths =
                ((MamdaniEngine)engine).getRuleStrengths(fuzzified, ruleBase, andOp, orOp);

        int ruleNum = 1;
        for (Map.Entry<FuzzyRule, Double> entry : ruleStrengths.entrySet()) {
            FuzzyRule rule = entry.getKey();
            double strength = entry.getValue();

            System.out.println("\nRule " + ruleNum++ + ":");
            System.out.println("  IF " + formatAntecedents(rule.getAntecedents(), rule.getOperator()));
            System.out.println("  THEN GreenDuration is " + rule.getConsequentFuzzySet());
            System.out.println("  Firing Strength: " + String.format("%.4f", strength));
            System.out.println("  Weight: " + rule.getWeight());
        }

        System.out.println("\nPhase 3: IMPLICATION & AGGREGATION");
        FuzzySet aggregated = engine.inferAndAggregate(fuzzified, ruleBase, duration, andOp, orOp);
        System.out.println("All rule consequents clipped and aggregated");

        System.out.println("\nPhase 4: DEFUZZIFICATION");
        double output = defuzzMethod.defuzzify(aggregated, duration.getMinRange(), duration.getMaxRange());

        System.out.println("\n" + "=".repeat(60));
        System.out.println("FINAL RESULTS");
        System.out.println("=".repeat(60));

        System.out.println("\nInput Summary:");
        System.out.println("  Traffic Density: " + crispInputs.get("Density"));
        System.out.println("  Waiting Time: " + crispInputs.get("WaitingTime") + " seconds");

        System.out.println("\nOutput:");
        System.out.println("  Green Light Duration: " + String.format("%.2f", output) + " seconds");

        System.out.println("\nConfiguration Used:");
        System.out.println("  Inference Engine: " + engine.getClass().getSimpleName());
        System.out.println("  AND Operator: " + andOp.getClass().getSimpleName());
        System.out.println("  OR Operator: " + orOp.getClass().getSimpleName());
        System.out.println("  Defuzzification: " + defuzzMethod.getClass().getSimpleName());
        System.out.println("  Active Rules: " + ruleBase.getActiveRuleCount() + "/" + ruleBase.size());

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Inference Complete");
        System.out.println("=".repeat(60));
    }

    private static int readIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a value between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double readDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a value between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String formatMemberships(Map<String, Double> memberships) {
        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<String, Double> entry : memberships.entrySet()) {
            if (i++ > 0) sb.append(", ");
            sb.append(entry.getKey()).append("=")
                    .append(String.format("%.3f", entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }
}