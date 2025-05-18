package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEventValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEventValid {

    String message() default "Event validation failed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}