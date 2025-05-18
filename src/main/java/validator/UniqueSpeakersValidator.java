package validator;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueSpeakersValidator implements ConstraintValidator<UniqueSpeakersValid, Event> {

    @Override
    public void initialize(UniqueSpeakersValid constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event == null || event.getSpeakers() == null) {
            return true;
        }

        List<String> speakers = event.getSpeakers();
        Set<String> uniqueSpeakers = new HashSet<>(speakers);

        if (uniqueSpeakers.size() < speakers.size()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{event.speakers.unique}")
                    .addPropertyNode("speakers")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}