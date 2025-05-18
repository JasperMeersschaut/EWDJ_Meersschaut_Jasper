package validator;

import domain.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UniqueEventValidator implements ConstraintValidator<UniqueEventValid, Event> {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void initialize(UniqueEventValid constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event == null || event.getEventDateTime() == null || event.getRoom() == null || event.getName() == null) {
            return true; // Let other validators handle null checks
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        // Check for duplicate event at same time in same room
        if (eventRepository.existsByRoomAndEventDateTime(event.getRoom(), event.getEventDateTime())) {
            context.buildConstraintViolationWithTemplate("{event.room.time.conflict}")
                    .addPropertyNode("eventDateTime")
                    .addConstraintViolation();
            isValid = false;
        }

        // Check for duplicate event name on the same day
        LocalDate eventDate = event.getEventDateTime().toLocalDate();
        LocalDateTime startOfDay = eventDate.atStartOfDay();
        LocalDateTime endOfDay = eventDate.plusDays(1).atStartOfDay().minusSeconds(1);

        if (eventRepository.existsByNameAndEventDateTimeBetween(
                event.getName(), startOfDay, endOfDay)) {
            context.buildConstraintViolationWithTemplate("{event.name.duplicate}")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}