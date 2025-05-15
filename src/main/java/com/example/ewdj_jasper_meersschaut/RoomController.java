package com.example.ewdj_jasper_meersschaut;

import domain.Room;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    @PostMapping
    public String addRoom(@ModelAttribute @Valid Room room, BindingResult result) {
        if (result.hasErrors()) {
            return "rooms/form";
        }
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }
}
