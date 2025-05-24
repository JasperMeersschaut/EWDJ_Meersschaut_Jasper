package com.example.ewdj_jasper_meersschaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginPageAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void loginFailure() throws Exception {
        mockMvc.perform(formLogin().user("invalidUser").password("invalidPassword"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithAnonymousUser
    public void anonymousCanAccessPublicPages() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void anonymousCannotAccessCreateEvent() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void userCannotAccessAdminPages() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminCanAccessAdminPages() throws Exception {
        mockMvc.perform(get("/events/create"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rooms/create"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    public void userCanAccessFavorites() throws Exception {
        mockMvc.perform(get("/favourites"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void anonymousCannotAccessFavorites() throws Exception {
        mockMvc.perform(get("/favourites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void authenticatedUser() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void accessDeniedPageWorks() throws Exception {
        mockMvc.perform(get("/403"))
                .andExpect(status().isOk())
                .andExpect(view().name("403"));
    }

    @Test
    public void notFoundPageWorks() throws Exception {
        mockMvc.perform(get("/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("404"));
    }

}