package com.example.ewdj_jasper_meersschaut;

import domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.FavouriteService;

@Controller
@RequestMapping("/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping
    public String getUserFavorites(Model model, User user) {
        model.addAttribute("favourites", favouriteService.getSortedFavorites(user));
        return "events/favouritesList";
    }
}
