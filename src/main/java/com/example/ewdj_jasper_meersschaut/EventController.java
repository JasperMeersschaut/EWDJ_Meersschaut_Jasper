package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import domain.Room;
import domain.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import repository.UserRepository;
import service.EventService;
import service.FavouriteService;
import service.RoomService;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    private final RoomService roomService;

    public EventController(EventService eventService, RoomService roomService) {
        this.eventService = eventService;
        this.roomService = roomService;
    }

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("eventsList", eventService.getAllEvents());
        return "eventsList";
    }


    @Autowired
    private FavouriteService favouriteService;


    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model, Authentication authentication) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);

        boolean isFavourite = false;
        int favouriteCount = 0;
        boolean maxFavouritesReached = false;
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userRepository.findUserByUsername(username).orElse(null);
            if (user != null) {
                isFavourite = user.getFavourites().contains(event);
                favouriteCount = user.getFavourites().size();
                maxFavouritesReached = favouriteCount >= 5;
            }
        }
        model.addAttribute("isFavourite", isFavourite);
        model.addAttribute("favouriteCount", favouriteCount);
        model.addAttribute("maxFavouritesReached", maxFavouritesReached);

        return "eventDetails";
    }

    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", Event.EventFactory.createEvent());
        model.addAttribute("rooms", roomService.findAll());
        return "events/form";
    }

    @PostMapping("/events/create")
    public String addEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, @RequestParam("roomId") Long roomId, Model model, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.findAll());
            return "event-form";
        }

        try {
            Room room = roomService.findById(roomId);
            event.setRoom(room);
            eventService.save(event);
            attributes.addFlashAttribute("success", "Event added successfully");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }
    }

}
