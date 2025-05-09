package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import repository.EventRepository;

@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventRepository repository;

    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("eventsList", repository.findAll());
        return "eventsList";
    }

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        model.addAttribute("event", repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + id)));
        return "eventDetails";
    }

    @PostMapping
    public String createEvent(Event event, Model model) {
        model.addAttribute("eventSave",  repository.save(event));
        return "redirect:/events";
    }


//    @GetMapping("/{id}")
//    public String viewEvent(@PathVariable Long id, Model model) {
//        Event event = eventService.getEventById(id);
//        model.addAttribute("event", event);
//        return "events/view";
//    }
//
//    @PostMapping
//    public String addEvent(@ModelAttribute @Valid Event event, BindingResult result) {
//        if (result.hasErrors()) {
//            return "events/form";
//        }
//        eventService.saveEvent(event);
//        return "redirect:/events";
//    }
}
