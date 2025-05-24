package com.example.ewdj_jasper_meersschaut.validator;

import domain.Event;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import validator.UniqueSpeakersValid;
import validator.UniqueSpeakersValidator;

import java.util.Arrays;
import java.util.List;
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
    void eventWithEmptySpeakers_shouldHaveSpeakersRequiredViolation() {
        Event event = new Event();
        event.setSpeakers(List.of("", " ", ""));

        Set<ConstraintViolation<Event>> violations = validator.validate(event);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("speakers")
                        && v.getMessage().equals("{event.speakers.required}"));
    }

    @Test
    void isValid_withNullEvent_shouldReturnTrue() {
        UniqueSpeakersValidator validator = new UniqueSpeakersValidator();
        assertThat(validator.isValid(null, null)).isTrue();
    }
}