package io.github.George_Al3xander.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<ChronologicalDates, Object> {

    private String fromField;
    private String toField;

    @Override
    public void initialize(ChronologicalDates constraintAnnotation) {
        this.fromField = constraintAnnotation.from();
        this.toField = constraintAnnotation.to();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapper wrapper = new BeanWrapperImpl(value);

        Object fromValue = wrapper.getPropertyValue(fromField);
        Object toValue = wrapper.getPropertyValue(toField);

        if (fromValue == null || toValue == null) {
            return true;
        }

        if (!(fromValue instanceof LocalDateTime fromDate)
                || !(toValue instanceof LocalDateTime toDate)) {
            return false;
        }

        return fromDate.isBefore(toDate);
    }
}