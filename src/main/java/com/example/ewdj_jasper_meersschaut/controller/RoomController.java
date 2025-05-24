package com.example.ewdj_jasper_meersschaut.controller;

import domain.Room;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", Room.RoomFactory.createRoom());
        return "rooms/form";
    }

    @PostMapping("/create")
    public String addRoom(@ModelAttribute @Valid Room room, BindingResult result, RedirectAttributes redirectAttributes) {
        if (roomService.existsByName(room.getName())) {
            result.rejectValue("name", "room.name.duplicate", "A room with this name already exists");
        }

        if (result.hasErrors()) {
            return "rooms/form";
        }

        try {
            roomService.save(room);
            redirectAttributes.addFlashAttribute("successMessage", "Room " + room.getName() + " with capacity " + room.getCapacity() + " has been successfully created");
            return "redirect:/events";
        } catch (Exception e) {
            return "rooms/form";
        }
    }
}