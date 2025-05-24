package com.example.ewdj_jasper_meersschaut.validator;

import domain.Event;
import domain.Room;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import repository.EventRepository;
import validator.UniqueEventValidator;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueEventValidatorTest {

    private Validator validator;
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        eventRepository = Mockito.mock(EventRepository.class);
    }

    private Event createEvent(String name, LocalDateTime dateTime, Room room) {
        Event event = new Event();
        event.setName(name);
        event.setEventDateTime(dateTime);
        event.setRoom(room);
        return event;
    }


    @Test
    void eventWithNoConflicts_shouldHaveNoViolations() {
        Event event = createEvent("Event1", LocalDateTime.now(), new Room());

        Mockito.when(eventRepository.existsByRoomAndEventDateTime(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(eventRepository.existsByNameAndEventDateTimeBetween(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(false);

        UniqueEventValidator validatorInstance = new UniqueEventValidator();
        ReflectionTestUtils.setField(validatorInstance, "eventRepository", eventRepository);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertThat(validatorInstance.isValid(event, context)).isTrue();
    }

    @Test
    void eventWithRoomTimeConflict_shouldHaveViolations() {
        Event event = createEvent("Event1", LocalDateTime.now(), new Room());

        Mockito.when(eventRepository.existsByRoomAndEventDateTime(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(eventRepository.existsByNameAndEventDateTimeBetween(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(false);

        UniqueEventValidator validatorInstance = new UniqueEventValidator();
        ReflectionTestUtils.setField(validatorInstance, "eventRepository", eventRepository);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertThat(validatorInstance.isValid(event, context)).isFalse();
    }

    @Test
    void eventWithNameConflict_shouldHaveViolations() {
        Event event = createEvent("Event1", LocalDateTime.now(), new Room());

        Mockito.when(eventRepository.existsByRoomAndEventDateTime(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(eventRepository.existsByNameAndEventDateTimeBetween(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(true);

        UniqueEventValidator validatorInstance = new UniqueEventValidator();
        ReflectionTestUtils.setField(validatorInstance, "eventRepository", eventRepository);

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));

        assertThat(validatorInstance.isValid(event, context)).isFalse();
    }

    @Test
    void isValid_withNullEvent_shouldReturnTrue() {
        UniqueEventValidator validatorInstance = new UniqueEventValidator();
        assertThat(validatorInstance.isValid(null, null)).isTrue();
    }
}