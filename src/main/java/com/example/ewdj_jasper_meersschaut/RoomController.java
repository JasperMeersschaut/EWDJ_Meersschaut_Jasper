//package com.example.ewdj_jasper_meersschaut;
//
//import domain.Room;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import repository.RoomRepository;
//import service.RoomService;
//
//@Controller
//@RequestMapping("/rooms")
//public class RoomController {
//    @Autowired
//    private RoomRepository repository;
//
//    @PostMapping
//    public String addRoom(@ModelAttribute @Valid Room room, BindingResult result) {
//        if (result.hasErrors()) {
//            return "rooms/form";
//        }
//        roomService.saveRoom(room);
//        return "redirect:/rooms";
//    }
//}
