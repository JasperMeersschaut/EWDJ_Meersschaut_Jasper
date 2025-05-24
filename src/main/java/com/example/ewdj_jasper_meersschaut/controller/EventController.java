package com.example.ewdj_jasper_meersschaut.controller;

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
import service.RoomService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
    private UserRepository userRepository;

    /**
     * Toont de details van een specifieke event.
     * Indien de gebruiker is ingelogd, worden ook de favoriete status en het aantal favorieten getoond.
     * Indien de gebruiker meer dan 5 favorieten heeft, wordt dit ook meegegeven.
     */

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model, Authentication authentication) {
        try {
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
        } catch (Exception e) {
            return "redirect:/404";
        }
    }

    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", Event.EventFactory.createEvent());
        model.addAttribute("rooms", roomService.findAll());
        return "events/form";
    }

    /**
     * Voegt een nieuwe event toe.
     * Controleert of de event al bestaat in de database en of er geen tijdsconflicten zijn met andere events in dezelfde zaal.
     * Indien er fouten zijn, worden deze weergegeven op het formulier.
     * Bij succes wordt de gebruiker doorgestuurd naar de lijst van events met een succesbericht.
     */
    @PostMapping("/create")
    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result, @RequestParam("roomId") Long roomId, Model model, RedirectAttributes redirectAttributes) {
        Room room = roomService.findById(roomId);
        event.setRoom(room);
        event.setSpeakers(event.getSpeakers()
                .stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList()));
        if (eventService.existsByRoomAndEventDateTime(event.getRoom(), event.getEventDateTime())) {
            result.rejectValue("eventDateTime", "event.room.time.conflict", "Another event is already scheduled in this room at the same time");
        }

        if (event.getEventDateTime() != null) {
            LocalDate eventDate = event.getEventDateTime().toLocalDate();
            LocalDateTime startOfDay = eventDate.atStartOfDay();
            LocalDateTime endOfDay = eventDate.plusDays(1).atStartOfDay().minusSeconds(1);

            if (eventService.existsByNameAndEventDateTimeBetween(event.getName(), startOfDay, endOfDay)) {
                result.rejectValue("name", "event.name.duplicate", "An event with this name already exists on the same day");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }

        try {
            eventService.save(event);
            redirectAttributes.addFlashAttribute("successMessage", "Event " + event.getName() + " has been successfully created");
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        try {
            Event event = eventService.findById(id);
            model.addAttribute("event", event);
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        } catch (Exception e) {
            return "redirect:/404";
        }
    }

    /**
     * Verwerkt de update van een bestaande event.
     * Controleert of de event al bestaat in de database en of er geen tijdsconflicten zijn met andere events in dezelfde zaal.
     * Indien er fouten zijn, worden deze weergegeven op het formulier.
     * Bij succes wordt de gebruiker doorgestuurd naar de lijst van events met een succesbericht.
     */
    @PostMapping("/{id}/update")
    public String updateEvent(@PathVariable Long id, @Valid @ModelAttribute("event") Event event, BindingResult bindingResult, @RequestParam("roomId") Long roomId, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }

        event.setRoom(roomService.findById(roomId));
        event.setId(id);

        try {
            eventService.save(event);
            redirectAttributes.addFlashAttribute("successMessage", "Event " + event.getName() + " has been successfully updated");
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }
    }

}
