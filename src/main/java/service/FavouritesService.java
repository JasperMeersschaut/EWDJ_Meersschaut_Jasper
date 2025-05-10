package service;

import domain.Event;
import domain.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FavouritesService {
    public List<Event> getSortedFavorites(User user) {
        if (user == null) {
            return Collections.emptyList();
        }

        return user.getFavourites().stream()
                .sorted(Comparator.comparing(Event::getEventDateTime)
                        .thenComparing(Event::getName))
                .collect(Collectors.toList());
    }
}
