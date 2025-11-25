package ro.unibuc.fmi.karate_management_platform.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ro.unibuc.fmi.karate_management_platform.validators.MaxCategoryPerKeyValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxCategoryPerKeyValidator.class)
public @interface MaxCategoriesPerKey {
    String message() default "Each category set must contain at most {value} items";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value() default 2; // default maximum size

}
