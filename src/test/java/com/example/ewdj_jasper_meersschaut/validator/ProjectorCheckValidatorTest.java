package com.example.ewdj_jasper_meersschaut.validator;

import domain.Event;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import validator.ProjectorCheckValid;
import validator.ProjectorCheckValidator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectorCheckValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void eventWithValidProjectorCheck_shouldHaveNoViolations() {
        Event event = new Event();
        event.setProjectorCode(1234);
        event.setProjectorCheck(1234 % 97);

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof ProjectorCheckValid)
                .isEmpty();
    }

    @Test
    void eventWithInvalidProjectorCheck_shouldHaveViolations() {
        Event event = new Event();
        event.setProjectorCode(1234);
        event.setProjectorCheck(1);

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof ProjectorCheckValid)
                .isNotEmpty();
    }

    @Test
    void eventWithZeroProjectorCode_shouldHaveNoViolations() {
        Event event = new Event();
        event.setProjectorCode(0);
        event.setProjectorCheck(0);

        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertThat(violations)
                .filteredOn(v -> v.getConstraintDescriptor().getAnnotation() instanceof ProjectorCheckValid)
                .isEmpty();
    }

    @Test
    void isValid_withNullEvent_shouldReturnTrue() {
        ProjectorCheckValidator validator = new ProjectorCheckValidator();
        assertThat(validator.isValid(null, null)).isTrue();
    }
}