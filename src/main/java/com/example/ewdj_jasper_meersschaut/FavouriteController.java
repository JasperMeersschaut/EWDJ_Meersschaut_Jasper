package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.UserRepository;
import service.FavouriteService;

@Controller
@RequestMapping("/favourites")
public class FavouriteController {

    private static final Logger logger = LoggerFactory.getLogger(FavouriteController.class);


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

//    @PostMapping("/add")
//    public String addFavourite(@RequestParam Long eventId, Authentication authentication) {
//        String username = authentication.getName();
//        User user = userRepository.findUserByUsername(username).orElse(null);
//        if (user != null && user.getFavourites().size() < 5) {
//            Event event = favouriteService.findEventById(eventId);
//            if (event != null) {
//                user.getFavourites().add(event);
//                userRepository.save(user);
//            }
//        }
//        return "redirect:/events/" + eventId;
//    }

    @PostMapping("/add")
    public String addFavourite(@RequestParam Long eventId, Authentication authentication) {
        logger.info("POST /favourites/add called with eventId={}, user={}", eventId, authentication != null ? authentication.getName() : "anonymous");
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username).orElse(null);
        if (user != null) {
            logger.info("User found: {}", user.getUsername());
            logger.info("Current favourites count: {}", user.getFavourites().size());
            if (user.getFavourites().size() < 5) {
                Event event = favouriteService.findEventById(eventId);
                if (event != null) {
                    logger.info("Event found: {}", event.getName());
                    // ... add to favourites logic
                } else {
                    logger.warn("Event not found for id={}", eventId);
                }
            } else {
                logger.warn("User {} has reached max favourites", user.getUsername());
            }
        } else {
            logger.warn("User not found: {}", username);
        }
        return "redirect:/events/" + eventId;
    }

    @PostMapping("/remove")
    public String removeFavourite(@RequestParam Long eventId, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username).orElse(null);
        if (user != null) {
            Event event = favouriteService.findEventById(eventId);
            if (event != null) {
                user.getFavourites().remove(event);
                userRepository.save(user);
            }
        }
        return "redirect:/events/" + eventId;
    }
}