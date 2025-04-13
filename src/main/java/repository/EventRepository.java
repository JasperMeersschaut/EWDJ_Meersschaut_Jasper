package repository;

import domain.Event;
import domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByRoomAndEventDateTime(Room room, LocalDateTime eventDateTime);
}
