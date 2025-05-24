package validator;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueSpeakersValidator implements ConstraintValidator<UniqueSpeakersValid, Event> {


    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event == null || event.getSpeakers() == null) {
            return true;
        }

        List<String> speakers = event.getSpeakers();
        Set<String> uniqueSpeakers = new HashSet<>();
        boolean hasNonEmpty = false;

        for (String speaker : speakers) {
            if (speaker != null && !speaker.trim().isEmpty()) {
                hasNonEmpty = true;
                if (!uniqueSpeakers.add(speaker.trim())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{event.speakers.unique}")
                            .addPropertyNode("speakers")
                            .addConstraintViolation();
                    return false;
                }
            }
        }

        if (!hasNonEmpty) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{event.speakers.required}")
                    .addPropertyNode("speakers")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}