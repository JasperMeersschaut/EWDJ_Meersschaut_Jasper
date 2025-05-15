package com.example.ewdj_jasper_meersschaut;

import domain.Room;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PostMapping
    public String addRoom(@ModelAttribute @Valid Room room, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rooms/form";
        }
        roomService.saveRoom(room);
        model.addAttribute("message", "Room '" + room.getName() + "' with capacity " + room.getCapacity() + " was added.");
        return "rooms/form";
    }
}
