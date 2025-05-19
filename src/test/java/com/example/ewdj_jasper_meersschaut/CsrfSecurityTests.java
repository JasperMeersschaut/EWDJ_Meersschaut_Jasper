package com.example.ewdj_jasper_meersschaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import repository.UserRepository;
import service.EventService;
import service.UserService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
class CsrfSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCsrfProtection_withoutToken() throws Exception {
        mockMvc.perform(post("/events/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCsrfProtection_withToken() throws Exception {
        mockMvc.perform(post("/events/create")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}