package com.scproject.fuzzy.rulebase;

import com.scproject.fuzzy.core.FuzzyRule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RuleBase {

    private final List<FuzzyRule> rules;


    public RuleBase() {
        this.rules = new ArrayList<>();
    }


    public void addRule(FuzzyRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        rules.add(rule);
    }

    public void removeRule(int index) {
        if (index < 0 || index >= rules.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of bounds. Rule base size: " + rules.size()
            );
        }
        rules.remove(index);
    }


    public boolean removeRule(FuzzyRule rule) {
        return rules.remove(rule);
    }


    public FuzzyRule getRule(int index) {
        if (index < 0 || index >= rules.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of bounds. Rule base size: " + rules.size()
            );
        }
        return rules.get(index);
    }

    public List<FuzzyRule> getAllRules() {
        return new ArrayList<>(rules);
    }


    public List<FuzzyRule> getActiveRules() {
        return rules.stream()
                .filter(FuzzyRule::isEnabled)
                .collect(Collectors.toList());
    }

    public int size() {
        return rules.size();
    }


    public boolean isEmpty() {
        return rules.isEmpty();
    }

    public void clear() {
        rules.clear();
    }


    public int getActiveRuleCount() {
        return (int) rules.stream()
                .filter(FuzzyRule::isEnabled)
                .count();
    }


    public boolean contains(FuzzyRule rule) {
        return rules.contains(rule);
    }


    public int indexOf(FuzzyRule rule) {
        return rules.indexOf(rule);
    }


    public FuzzyRule replaceRule(int index, FuzzyRule newRule) {
        if (index < 0 || index >= rules.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of bounds. Rule base size: " + rules.size()
            );
        }
        if (newRule == null) {
            throw new IllegalArgumentException("New rule cannot be null");
        }
        return rules.set(index, newRule);
    }


    public void enableAllRules() {
        for (FuzzyRule rule : rules) {
            rule.setEnabled(true);
        }
    }

    public void disableAllRules() {
        for (FuzzyRule rule : rules) {
            rule.setEnabled(false);
        }
    }

    public void setWeightForAllRules(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }
        for (FuzzyRule rule : rules) {
            rule.setWeight(weight);
        }
    }


    @Override
    public String toString() {
        return "RuleBase{" +
                "totalRules=" + rules.size() +
                ", activeRules=" + getActiveRuleCount() +
                '}';
    }


    public void printRules() {
        System.out.println("Rule Base (" + rules.size() + " rules, " +
                getActiveRuleCount() + " active):");
        System.out.println("=".repeat(70));

        if (rules.isEmpty()) {
            System.out.println("  (No rules defined)");
        } else {
            for (int i = 0; i < rules.size(); i++) {
                FuzzyRule rule = rules.get(i);
                String status = rule.isEnabled() ? "[ENABLED]" : "[DISABLED]";
                System.out.printf("Rule %d: %s%n", i, status);
                System.out.printf("  %s%n", rule.toString());
                System.out.printf("  Weight: %.2f%n", rule.getWeight());
                if (i < rules.size() - 1) {
                    System.out.println();
                }
            }
        }

        System.out.println("=".repeat(70));
    }
}