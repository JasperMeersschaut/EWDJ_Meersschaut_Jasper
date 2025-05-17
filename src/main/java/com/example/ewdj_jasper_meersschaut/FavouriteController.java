package com.example.ewdj_jasper_meersschaut;

import domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.UserRepository;
import service.FavouriteService;

@Controller
@RequestMapping("/favourites")
public class FavouriteController {

    private final FavouriteService favouriteService;
    private final UserRepository userRepository;

    public FavouriteController(FavouriteService favouriteService, UserRepository userRepository) {
        this.favouriteService = favouriteService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getUserFavorites(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username).orElse(null);
        model.addAttribute("favourites", favouriteService.getSortedFavorites(user));
        return "events/favouritesList";
    }
}