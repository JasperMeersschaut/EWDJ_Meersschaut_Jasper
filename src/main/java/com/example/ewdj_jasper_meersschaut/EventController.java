package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.EventService;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("eventsList", eventService.getAllEvents());
        return "eventsList";
    }

    @GetMapping("/favourites")
    public String getUserFavorites(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Event> favorieten = eventService.getUserFavorites(username);
        model.addAttribute("favorieten", favorieten);
        return "events/favouritesList";
    }

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        return "eventDetails";
    }


    @PostMapping
    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result) {
        if (result.hasErrors()) {
            return "events/form";
        }
        eventService.saveEvent(event);
        return "redirect:/events";
    }
}
