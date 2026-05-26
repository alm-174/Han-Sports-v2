package com.javaweb.util.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
<<<<<<< HEAD
        if (value == null) {
            return true;
        }
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        // check if string contains at least one digit, one lowercase letter, one
        // uppercase letter, one special character and 8 characters long
        return value.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$");
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
