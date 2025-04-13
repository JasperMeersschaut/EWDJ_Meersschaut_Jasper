//package service;
//
//import domain.Event;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import repository.EventRepository;
//
//import java.util.List;
//
//@Service
//public class EventService {
//    @Autowired
//    private EventRepository eventRepository;
//
//    public List<Event> getAllEvents() {
//        return eventRepository.findAll(Sort.by("eventDateTime"));
//    }
//
//    public Event saveEvent(Event event) {
//        if (eventRepository.existsByRoomAndEventDateTime(event.getRoom(), event.getEventDateTime())) {
//            throw new IllegalArgumentException("Event already exists in the same room at the same time.");
//        }
//        return eventRepository.save(event);
//    }
//}
