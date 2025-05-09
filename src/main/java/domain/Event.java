package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{event.name.required}")
    @Pattern(regexp = "^[A-Za-z].*", message = "{event.name.invalid}")
    @Getter
    private String name;

    private String description;

    @ElementCollection
    @Size(max = 3, message = "{event.speakers.limit}")
    private List<String> speakers;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotNull(message = "{event.date.required}")
    @Getter
    private LocalDateTime eventDateTime;

    @Min(value = 1000, message = "{event.projectorCode.invalid}")
    @Max(value = 9999, message = "{event.projectorCode.invalid}")
    private int projectorCode;

    private int projectorCheck;

    @DecimalMin(value = "9.99", message = "{event.price.invalid}")
    @DecimalMax(value = "100.00", message = "{event.price.invalid}")
    private int price;

    public Event(String name, String description, List<String> speakers, Room room, LocalDateTime eventDateTime, int projectorCode, int projectorCheck, int price) {
        this.name = name;
        this.description = description;
        this.speakers = speakers;
        this.room = room;
        this.eventDateTime = eventDateTime;
        this.projectorCode = projectorCode;
        this.projectorCheck = projectorCheck;
        this.price = price;
    }

    public static class EventFactory {
        public static Event createEvent() {
            return new Event();
        }
    } }