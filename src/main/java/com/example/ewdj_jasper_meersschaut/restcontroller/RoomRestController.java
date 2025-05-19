package com.example.ewdj_jasper_meersschaut.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.RoomService;

@RestController
@RequestMapping("/rest/room")
public class RoomRestController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/capacity/{id}")
    public int getCapacity(@PathVariable("id") int roomId) {
        return roomService.findCapacityById((long) roomId);
    }
}