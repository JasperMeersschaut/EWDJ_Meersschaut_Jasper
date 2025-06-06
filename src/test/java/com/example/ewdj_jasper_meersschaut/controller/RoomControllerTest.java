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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void showCreateRoomForm_asAdmin_shouldReturnFormView() throws Exception {
        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().attributeExists("room"));
    }

    @Test
    @WithAnonymousUser
    void showCreateRoomForm_asAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void showCreateRoomForm_asUser_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addRoom_withValidData_shouldRedirectToEvents() throws Exception {
        String uniqueName = "Z" + String.format("%03d", (int) (Math.random() * 900) + 100);

        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", uniqueName)
                        .param("capacity", "25"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addRoom_withInvalidName_shouldReturnFormWithErrors() throws Exception {
        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", "")
                        .param("capacity", "25"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("room", "name"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addRoom_withInvalidCapacity_shouldReturnFormWithErrors() throws Exception {
        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", "Valid Room")
                        .param("capacity", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("room", "capacity"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addRoom_withCapacityTooLarge_shouldReturnFormWithErrors() throws Exception {
        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", "Valid Room")
                        .param("capacity", "51")) // Exceeds max capacity
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("room", "capacity"));
    }

    @Test
    @WithAnonymousUser
    void addRoom_asAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/rooms/create")  // Changed from /rooms to /rooms/create
                        .with(csrf())
                        .param("name", "Test Room")
                        .param("capacity", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void addRoom_asUser_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", "Test Room")
                        .param("capacity", "25"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addRoom_withDuplicateName_shouldReturnFormWithErrors() throws Exception {
        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", "A123")
                        .param("capacity", "25"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/rooms/create")
                        .with(csrf())
                        .param("name", "A123")
                        .param("capacity", "30"))
                .andExpect(status().isOk()) // This should return the form
                .andExpect(view().name("rooms/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("room", "name"));
    }
}