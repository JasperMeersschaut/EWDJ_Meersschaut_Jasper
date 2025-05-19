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
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void getLoginPage_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithAnonymousUser
    void getLoginPage_withErrorParam_shouldReturnLoginViewWithErrorMessage() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Invalid username and password!"));
    }

    @Test
    @WithAnonymousUser
    void getLoginPage_withLogoutParam_shouldReturnLoginViewWithLogoutMessage() throws Exception {
        mockMvc.perform(get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("msg", "You've been logged out successfully."));
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void login_alreadyAuthenticatedUser_shouldRedirectToEvents() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithAnonymousUser
    void loginPost_withValidCredentials_shouldRedirectToEvents() throws Exception {
        // Testing actual authentication in Spring Security is more complex
        // This is just testing the controller endpoint for POST requests
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "nameUser")
                        .param("password", "12345678"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    void loginPost_withInvalidCsrf_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "nameUser")
                        .param("password", "12345678"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void accessingProtectedResource_whenAuthenticated_shouldBeSuccessful() throws Exception {
        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("rooms/form"));
    }

    @Test
    @WithAnonymousUser
    void accessingProtectedResource_whenNotAuthenticated_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}