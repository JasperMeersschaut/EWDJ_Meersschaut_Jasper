package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoomConstraintValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoom {
    String message() default "Invalid room data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
