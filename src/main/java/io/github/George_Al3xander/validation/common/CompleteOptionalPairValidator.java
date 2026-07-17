package io.github.George_Al3xander.validation.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class CompleteOptionalPairValidator
        implements ConstraintValidator<CompleteOptionalPair, Object> {

    private String firstProperty;
    private String secondProperty;

    @Override
    public void initialize(CompleteOptionalPair annotation) {
        this.firstProperty = annotation.first();
        this.secondProperty = annotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapper wrapper = new BeanWrapperImpl(value);

        String first = (String) wrapper.getPropertyValue(firstProperty);
        String second = (String) wrapper.getPropertyValue(secondProperty);

        boolean firstPresent = first != null && !first.isBlank();
        boolean secondPresent = second != null && !second.isBlank();

        return firstPresent == secondPresent;
    }
}
