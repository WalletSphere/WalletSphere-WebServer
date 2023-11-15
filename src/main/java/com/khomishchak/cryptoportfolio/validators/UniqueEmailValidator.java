package com.khomishchak.cryptoportfolio.validators;

import com.khomishchak.cryptoportfolio.repositories.UserRepository;
import com.khomishchak.cryptoportfolio.validators.annotations.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findByEmail(field).isEmpty();
    }
}
