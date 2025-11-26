package com.scproject.fuzzy.validation;

import com.scproject.fuzzy.core.FuzzyVariable;

import java.util.Scanner;

public class UserInputReader {

    private static final Scanner scanner = new Scanner(System.in);

    public static double readValidatedInput(FuzzyVariable variable) {

        while (true) {
            System.out.print("Enter value for " + variable.getName()
                    + " (" + variable.getMinRange() + " to " + variable.getMaxRange() + "): ");

            String input = scanner.nextLine();

            try {
                return InputValidator.validateNumericInput(input, variable);
            } catch (ValidationException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }
}
