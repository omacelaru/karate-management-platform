package ro.unibuc.fmi.karate_management_platform.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ro.unibuc.fmi.karate_management_platform.annotations.MaxCategoriesPerKey;

import java.util.Map;
import java.util.Set;

public class MaxCategoryPerKeyValidator implements ConstraintValidator<MaxCategoriesPerKey, Map<Long, Set<?>>> {

    private int maxSize;

    @Override
    public void initialize(MaxCategoriesPerKey constraintAnnotation) {
        this.maxSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Map<Long, Set<?>> value, ConstraintValidatorContext context) {
        if (value == null) return true;

        return value.values().stream()
                .allMatch(set -> set != null && set.size() <= maxSize);
    }
}