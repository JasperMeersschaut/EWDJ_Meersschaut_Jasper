package com.example.ewdj_jasper_meersschaut;

import domain.Room;
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
    public String addRoom(@ModelAttribute Room room, BindingResult result,
                          Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "rooms/form";
        }
        if (roomService.existsByName(room.getName())) {
            result.rejectValue("name", "duplicate", "Room name already exists");
            return "rooms/form";
        }
        try {
            roomService.save(room);
            attributes.addFlashAttribute("successMessage",
                    new Object[]{room.getName(), room.getCapacity()});
            return "redirect:/rooms/create";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "rooms/form";
        }
    }
}
