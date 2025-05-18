package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProjectorCheckValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectorCheckValid {

    String message() default "Beamer check is invalid. It must be the remainder of the beamer code divided by 97";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
