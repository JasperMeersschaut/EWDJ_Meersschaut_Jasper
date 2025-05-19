package com.example.ewdj_jasper_meersschaut.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithAnonymousUser
    void listEvents_shouldReturnEventsListView() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/eventsList"))
                .andExpect(model().attributeExists("eventsList"));
    }

    @Test
    @WithAnonymousUser
    void viewEvent_whenEventNotFound_shouldHandleError() throws Exception {
        // Note: This test will depend on how your controller handles not found cases
        mockMvc.perform(get("/events/999"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventDetails"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void showCreateEventForm_asAdmin_shouldReturnFormView() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("rooms"));
    }

    @Test
    @WithAnonymousUser
    void showCreateEventForm_asAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addEvent_withValidData_shouldRedirectToEvents() throws Exception {
        mockMvc.perform(post("/events/create")
                        .with(csrf())
                        .param("name", "Test Event")
                        .param("description", "Test Description")
                        .param("speakers[0]", "Speaker1")
                        .param("speakers[1]", "Speaker2")
                        .param("roomId", "1")
                        .param("eventDateTime", "2025-04-21T10:00:00")
                        .param("projectorCode", "1234")
                        .param("projectorCheck", "45")
                        .param("price", "19.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addEvent_withInvalidData_shouldReturnFormViewWithErrors() throws Exception {
        mockMvc.perform(post("/events/create")
                        .with(csrf())
                        .param("name", "")
                        .param("roomId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().hasErrors());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void showEditEventForm_asAdmin_shouldReturnEventFormView() throws Exception {
        mockMvc.perform(get("/events/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/form"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("rooms"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void showEditEventForm_asUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/events/1/edit"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateEvent_withValidData_shouldRedirectToEventsList() throws Exception {
        mockMvc.perform(post("/events/1/update")
                        .with(csrf())
                        .param("name", "Updated Event")
                        .param("description", "Updated Description")
                        .param("speakers[0]", "Speaker1")
                        .param("roomId", "1")
                        .param("eventDateTime", "2025-04-21T10:00:00")
                        .param("projectorCode", "1234")
                        .param("projectorCheck", "45")
                        .param("price", "29.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));
    }
}