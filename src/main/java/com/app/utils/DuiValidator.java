package com.app.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class DuiValidator {
    private DuiValidator(){}

    public static boolean isValidDUI(String dui) {
        if (dui == null || !dui.matches("^\\d{8}-\\d$")) return false;
        try {
            String[] parts = dui.split("-");
            String numbers = parts[0];
            int validator = Integer.parseInt(parts[1]);
            int sum = 0;
            for (int i = 0; i < 8; i++) {
                sum += Character.getNumericValue(numbers.charAt(i)) * (9 - i);
            }
            int calculated = (10 - (sum % 10)) % 10;
            return validator == calculated;
        } catch (Exception e) {
            return false;
        }
    }
}
