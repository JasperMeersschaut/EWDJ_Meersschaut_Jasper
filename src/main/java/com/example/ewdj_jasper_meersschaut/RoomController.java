package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import domain.Room;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.EventService;
import service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    private final EventService eventService;

    public RoomController(RoomService roomService, EventService eventService) {
        this.roomService = roomService;
        this.eventService = eventService;
    }

    @GetMapping("/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", Room.RoomFactory.createRoom());
        return "rooms/form";
    }

    @PostMapping("/create")
    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result, @RequestParam("room.id") Long roomId, Model model) {
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

            eventService.save(event);
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomService.findAll());
            return "events/form";
        }

    }
}
