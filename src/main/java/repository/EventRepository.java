package repository;

import domain.Event;
import domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByRoomAndEventDateTime(Room room, LocalDateTime eventDateTime);

    List<Event> findAllByOrderByEventDateTimeAsc();
    List<Event> findByEventDateTimeBetween(LocalDateTime start, LocalDateTime end);
    boolean existsByNameAndEventDateTimeBetween(String name, LocalDateTime start, LocalDateTime end);
}