package com.example.ewdj_jasper_meersschaut;

import jakarta.activation.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import repository.UserRepository;
import service.EventService;
import service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(SecurityConfig.class)
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private EventService eventService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private DataSource dataSource;

    @ParameterizedTest
    @CsvSource({
            "/login, 200",
            "/css/style.css, 200",
            "/403, 200",
            "/404, 200",
            "/events, 200",
            "/events/1, 200"
    })

    public void testPublicEndpoints(String url, int expectedStatus) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().is(expectedStatus));
    }

    @org.junit.jupiter.api.Test
    public void testLoginRedirection() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/favourites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @org.junit.jupiter.api.Test
    @WithMockUser(roles = "USER")
    public void testUserAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/favourites")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    void testAnonymousUserCannotAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        mockMvc.perform(get("/events/favourites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void testUserCanAccessUserEndpoints() throws Exception {
        mockMvc.perform(get("/events/favourites"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/favourites/1/add"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/favourites/1/remove"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void testUserCannotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/events/1/edit"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/events/1/edit"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/events/1/update"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminCanAccessUserEndpoints() throws Exception {
        mockMvc.perform(get("/events/favourites"))
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    void testAccessDeniedHandling() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}