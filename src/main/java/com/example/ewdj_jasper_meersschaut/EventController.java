package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import repository.EventRepository;
import service.EventService;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService = new EventService();

    @Autowired
    private EventRepository repository;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("eventsList", repository.findAll());
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
        Event event = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + id));
        model.addAttribute("event", event);
        return "eventDetails";
    }


//
//    @PostMapping
//    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result) {
//        if (result.hasErrors()) {
//            return "events/form";
//        }
//        eventService.saveEvent(event);
//        return "redirect:/events";
//    }
}
