package service;

import domain.Event;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.EventRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavouriteService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    public FavouriteService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public List<Event> getSortedFavourites(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        return user.getFavourites().stream()
                .sorted(Comparator.comparing(Event::getEventDateTime)
                        .thenComparing(Event::getName))
                .collect(Collectors.toList());
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
}