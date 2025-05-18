package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueSpeakersValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSpeakersValid {

    String message() default "{event.speakers.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}