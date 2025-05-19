package com.example.ewdj_jasper_meersschaut.restcontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import service.RoomService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class RoomRestMockTest {

    @Mock
    private RoomService mockRoomService;

    private RoomRestController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new RoomRestController();
        mockMvc = standaloneSetup(controller).build();
        ReflectionTestUtils.setField(controller, "roomService", mockRoomService);
    }

    @Test
    void testGetCapacity_isOk() throws Exception {
        Mockito.when(mockRoomService.findCapacityById(1L)).thenReturn(42);

        mockMvc.perform(get("/rest/room/capacity/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(42));

        Mockito.verify(mockRoomService).findCapacityById(1L);
    }
}