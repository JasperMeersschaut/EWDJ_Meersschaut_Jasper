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

    @PostMapping
    public String addRoom(@Valid @ModelAttribute Room room, BindingResult result,
                          Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            //   model.addAttribute("rooms", roomService.getAllRooms());
            return "room-management";
        }
        if (roomService.existsByName(room.getName())) {
            result.rejectValue("name", "duplicate", "Room name already exists");
            // model.addAttribute("rooms", roomService.getAllRooms());
            return "room-management";
        }
        try {
            roomService.save(room);
            attributes.addFlashAttribute("success",
                    String.format("Room %s with capacity %d was added", room.getName(), room.getCapacity()));
            return "redirect:/";
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}
