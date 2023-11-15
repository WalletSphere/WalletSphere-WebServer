package com.khomishchak.cryptoportfolio.validators.annotations;

import com.khomishchak.cryptoportfolio.validators.UniqueUsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {

    String message() default "This username is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
