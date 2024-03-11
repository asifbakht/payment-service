package com.microservice.payment.helper.enumvalidator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    List<String> enums = null;

    @Override
    public boolean isValid(final String enumValue, final ConstraintValidatorContext context) {
        return enums.contains(enumValue.toUpperCase());
    }

    @Override
    public void initialize(final EnumValidator constraint) {
        Class<? extends Enum<?>> enumClass = constraint.enumClazz();
        enums = Stream.of(enumClass.getEnumConstants()).map(Enum::toString).toList();
    }

}
