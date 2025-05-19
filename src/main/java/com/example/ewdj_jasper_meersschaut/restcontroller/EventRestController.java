package com.example.ewdj_jasper_meersschaut.restcontroller;

import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rest/event")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @GetMapping("/date/{date}")
    public List<Event> getEventsByDate(@PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);
        return eventService.findByEventDateTimeBetween(startOfDay, endOfDay);
    }
}