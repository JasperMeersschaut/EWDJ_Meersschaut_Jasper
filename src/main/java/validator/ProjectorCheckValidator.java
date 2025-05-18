package validator;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProjectorCheckValidator implements ConstraintValidator<ProjectorCheckValid, Event> {

    @Override
    public void initialize(ProjectorCheckValid constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event == null || event.getProjectorCode() == 0) {
            return true;
        }

        int expectedCheck = event.getProjectorCode() % 97;
        boolean isValid = event.getProjectorCheck() == expectedCheck;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{event.projectorCheck.calculation}")
                    .addPropertyNode("projectorCheck")
                    .addConstraintViolation();
        }

        return isValid;
    }
}