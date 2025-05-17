package validator;

import domain.Room;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoomConstraintValidator implements ConstraintValidator<ValidRoom, Room> {

    @Override
    public void initialize(ValidRoom constraintAnnotation) {
    }

    @Override
    public boolean isValid(Room room, ConstraintValidatorContext context) {
        if (room == null) return true;

        boolean valid = true;

        if (room.getName() == null || !room.getName().matches("^[A-Za-z]\\d{3}$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{room.name.pattern}")
                    .addPropertyNode("name").addConstraintViolation();
            valid = false;
        }

        if (room.getCapacity() < 1 || room.getCapacity() > 505) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Capacity must be between 1 and 505")
                    .addPropertyNode("capacity").addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
