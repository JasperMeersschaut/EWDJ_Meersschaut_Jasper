package com.example.ewdj_jasper_meersschaut.restcontroller;

import domain.Event;
import domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import service.EventService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class EventRestMockTest {

    @Mock
    private EventService mockEventService;

    private EventRestController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EventRestController();
        mockMvc = standaloneSetup(controller).build();
        ReflectionTestUtils.setField(controller, "eventService", mockEventService);
    }

    @Test
    void testGetEventsByDate_emptyList() throws Exception {
        Mockito.when(mockEventService.findByEventDateTimeBetween(
                        Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rest/event/date/2024-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        Mockito.verify(mockEventService).findByEventDateTimeBetween(
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
    }

    @Test
    void testGetEventsByDate_nonEmptyList() throws Exception {
        Room room = new Room("A101", 30);
        Event event = new Event("Event1", "desc", List.of("Speaker1"), room,
                LocalDateTime.of(2024, 6, 10, 10, 0), 1234, 10, 19.99);

        Mockito.when(mockEventService.findByEventDateTimeBetween(
                        Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(event));

        mockMvc.perform(get("/rest/event/date/2024-06-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Event1"))
                .andExpect(jsonPath("$[0].room.name").value("A101"));

        Mockito.verify(mockEventService).findByEventDateTimeBetween(
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
    }
}