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

import java.time.LocalDate;
import java.time.LocalDateTime;

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
            model.addAttribute("error", "Error loading event details: " + e.getMessage());
            return "redirect:/404";
        }
    }

    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", Event.EventFactory.createEvent());
        model.addAttribute("rooms", roomService.findAll());
        return "events/form";
    }

    @PostMapping("/create")
    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result, @RequestParam("roomId") Long roomId, Model model, RedirectAttributes redirectAttributes) {
        Room room = roomService.findById(roomId);
        event.setRoom(room);

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
            model.addAttribute("error", e.getMessage());
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
            model.addAttribute("error", "Error loading event details: " + e.getMessage());
            return "redirect:/404";
        }

    }

    @PostMapping("/{id}/update")
    public String updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            @RequestParam("roomId") Long roomId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
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
