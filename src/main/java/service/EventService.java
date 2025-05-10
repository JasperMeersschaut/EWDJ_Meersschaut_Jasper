package service;

import domain.Event;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.EventRepository;
import repository.UserRepository;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Event> getUserFavorites(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getFavourites().stream()
                .sorted((e1, e2) -> {
                    int dateComparison = e1.getEventDateTime().compareTo(e2.getEventDateTime());
                    return dateComparison != 0 ? dateComparison : e1.getName().compareToIgnoreCase(e2.getName());
                })
                .toList();
    }
}