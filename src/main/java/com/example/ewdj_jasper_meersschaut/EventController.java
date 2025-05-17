package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.EventService;
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

//    @GetMapping("/favourites")
//    public String getUserFavorites(Authentication authentication, Model model) {
//        String username = authentication.getName();
//        List<Event> favorieten = eventService.getUserFavorites(username);
//        model.addAttribute("favorieten", favorieten);
//        return "events/favouritesList";
//    }

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        return "eventDetails";
    }

    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", Event.EventFactory.createEvent());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "events/form";
    }

    //    @PostMapping("/create")
//    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            model.addAttribute("rooms", roomService.getAllRooms());
//            return "events/form";
//        }
//        eventService.saveEvent(event);
//        return "redirect:/events";
//    }
    @PostMapping("/create")
    public String addEvent(@ModelAttribute Event event, Model model) {
        eventService.saveEvent(event);

        return "redirect:/events";
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
