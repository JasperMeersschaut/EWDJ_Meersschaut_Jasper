package com.example.ewdj_jasper_meersschaut.controller;

import domain.Event;
import domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.FavouriteService;
import service.UserService;

@Controller
@RequestMapping("/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final UserService userService;

    public FavouriteController(FavouriteService favouriteService, UserService userService) {
        this.favouriteService = favouriteService;
        this.userService = userService;
    }

    @GetMapping
    public String getUserFavourites(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("favourites", favouriteService.getSortedFavourites(user));
        return "events/favouritesList";
    }

    @PostMapping("/{id}/add")
    public String addFavourite(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Event event = favouriteService.findEventById(id);
        if (user != null && event != null) {
            if (user.getFavourites().size() < User.MAX_FAVOURITES) {
                user.getFavourites().add(event);
                userService.save(user);
            } else {
                //Knop uitgeschakeld indien max 5 favorieten, dus geen else nodig
            }
        }
        return "redirect:/events/" + id;
    }

    @PostMapping("/{id}/remove")
    public String removeFavourite(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Event event = favouriteService.findEventById(id);
        if (user != null && event != null) {
            user.getFavourites().remove(event);
            userService.save(user);
        }
        return "redirect:/events/" + id;
    }
}