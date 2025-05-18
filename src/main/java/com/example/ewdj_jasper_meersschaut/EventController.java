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
        return "events/eventsList";
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

    //    @PostMapping("/create")
//    public String addEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, @RequestParam("roomId") Long roomId, Model model, RedirectAttributes attributes) {
//        System.out.println(roomId);
//        if (result.hasErrors()) {
//            model.addAttribute("rooms", roomService.findAll());
//            return "events/form";
//        }
//
//        try {
//            Room room = roomService.findById(roomId);
//            event.setRoom(room);
//            eventService.save(event);
//            attributes.addFlashAttribute("success", String.format("Event %s was added", event.getName()));
//            return "redirect:/";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            model.addAttribute("rooms", roomService.findAll());
//            return "events/form";
//        }
//    }
    @PostMapping("/create")
    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result, @RequestParam("roomId") Long roomId, Model model) {
        System.out.println("Speakers: " + event.getSpeakers());
        System.out.println("Room: " + event.getRoom());
        System.out.println("Room id: " + roomId);
        System.out.println("Datetime: " + event.getEventDateTime());

        Room room = roomService.findById(roomId);
        event.setRoom(room);
        System.out.println("Room: " + room);
        System.out.println("Event room: " + event.getRoom());
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.findAll());
            System.out.println("Validation errors: " + result.getAllErrors());
            return "events/form";
        }
        try {
            System.out.println("Speakers: " + event.getSpeakers());
            System.out.println("Room: " + event.getRoom());
            System.out.println("Room id: " + roomId);
            System.out.println("Datetime: " + event.getEventDateTime());
            System.out.println("Event before save: " + event);
            eventService.save(event);
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }

    }

}
