package service;

import domain.Event;
import domain.Room;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.EventRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateTimeAsc();
    }

    public boolean existsByNameAndEventDateTimeBetween(String name, LocalDateTime start, LocalDateTime end) {
        return eventRepository.existsByNameAndEventDateTimeBetween(name, start, end);
    }

    public boolean existsByRoomAndEventDateTime(Room room, LocalDateTime eventDateTime) {
        return eventRepository.existsByRoomAndEventDateTime(room, eventDateTime);
    }


    @Transactional
    public void save(@Valid Event event) {
        eventRepository.save(event);
        System.out.println("Aantal events in DB: " + eventRepository.count());
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    public List<Event> findByEventDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByEventDateTimeBetween(start, end);
    }
}