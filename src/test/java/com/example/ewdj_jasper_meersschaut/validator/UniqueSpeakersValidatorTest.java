package validator;

import domain.Event;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueSpeakersValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void eventWithUniqueSpeakers_shouldHaveNoViolations() {
        Event event = new Event();
        event.setSpeakers(Arrays.asList("Alice", "Bob", "Charlie"));

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof UniqueSpeakersValid)
                .isEmpty();
    }

    @Test
    void eventWithDuplicateSpeakers_shouldHaveViolations() {
        Event event = new Event();
        event.setSpeakers(Arrays.asList("Alice", "Bob", "Alice"));

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof UniqueSpeakersValid)
                .isNotEmpty();
    }

    @Test
    void eventWithNullSpeakers_shouldHaveNoViolations() {
        Event event = new Event();
        event.setSpeakers(null);

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof UniqueSpeakersValid)
                .isEmpty();
    }

    @Test
    void eventWithEmptySpeakers_shouldHaveNoViolations() {
        Event event = new Event();
        event.setSpeakers(Collections.emptyList());

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof UniqueSpeakersValid)
                .isEmpty();
    }

    @Test
    void isValid_withNullEvent_shouldReturnTrue() {
        UniqueSpeakersValidator validator = new UniqueSpeakersValidator();
        assertThat(validator.isValid(null, null)).isTrue();
    }
}