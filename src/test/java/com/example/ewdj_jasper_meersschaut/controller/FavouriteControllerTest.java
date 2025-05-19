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
class FavouriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void getUserFavourites_asUser_shouldReturnFavouritesView() throws Exception {
        mockMvc.perform(get("/favourites"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/favouritesList"))
                .andExpect(model().attributeExists("favourites"));
    }

    @Test
    @WithAnonymousUser
    void getUserFavourites_asAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/favourites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }


    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void addFavourite_asUser_shouldRedirectToEventDetails() throws Exception {
        mockMvc.perform(post("/favourites/1/add")
                        .with(csrf()))
                .andExpect(redirectedUrl("/events/1"));
    }

    @Test
    @WithAnonymousUser
    void addFavourite_asAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/favourites/1/add")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void addFavourite_asAdmin_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/favourites/1/add")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void removeFavourite_asUser_shouldRedirectToEventDetails() throws Exception {
        mockMvc.perform(post("/favourites/1/remove")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/1"));
    }

    @Test
    @WithAnonymousUser
    void removeFavourite_asAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/favourites/1/remove")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void removeFavourite_asAdmin_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/favourites/1/remove")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void addFavourite_withoutCsrf_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/favourites/1/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "nameUser", roles = {"USER"})
    void removeFavourite_withoutCsrf_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/favourites/1/remove"))
                .andExpect(status().isForbidden());
    }
}