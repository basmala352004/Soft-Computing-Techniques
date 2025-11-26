package com.scproject.fuzzy.validation;

import com.scproject.fuzzy.core.FuzzyVariable;

public class InputValidator {

    public static double validateNumericInput(String value, FuzzyVariable variable) {

        // 1) Check null
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Input for " + variable.getName() + " cannot be null or empty.");
        }

        // 2) Check is number
        double numeric;
        try {
            numeric = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ValidationException(variable.getName() + " must be a numeric value.");
        }

        // 3) Check range
        if (numeric < variable.getMinRange() || numeric > variable.getMaxRange()) {
            throw new ValidationException(variable.getName()
                    + " must be between " + variable.getMinRange()
                    + " and " + variable.getMaxRange());
        }

        return numeric;
    }
}
