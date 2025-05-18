package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import validator.ProjectorCheckValid;
import validator.UniqueSpeakersValid;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id")
@ProjectorCheckValid
@UniqueSpeakersValid
@ToString(exclude = "id")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "{event.name.required}")
    @Pattern(regexp = "^[A-Za-z].*", message = "{event.name.invalid}")
    private String name;

    private String description;

    @ElementCollection
    @NotEmpty(message = "{event.speakers.required}")
    @Size(min = 1, max = 3, message = "{event.speakers.limit}")
    private List<String> speakers;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
//    @NotNull(message = "{event.room.required}")
    private Room room;

    @Future(message = "{event.date.future}")
    @NotNull(message = "{event.date.required}")
    private LocalDateTime eventDateTime;

    @NotNull(message = "{event.projectorCode.required}")
    @Min(value = 1000, message = "{event.projectorCode.invalid}")
    @Max(value = 9999, message = "{event.projectorCode.invalid}")
    private int projectorCode;

    @NotNull(message = "{event.projectorCheck.required}")
    @Min(value = 0, message = "{event.projectorCheck.invalid}")
    @Max(value = 96, message = "{event.projectorCheck.invalid}")
    private int projectorCheck;

    @NotNull(message = "{event.price.required}")
    @DecimalMin(value = "9.99", inclusive = true, message = "{event.price.unvalid}")
    @DecimalMax(value = "99.99", inclusive = false, message = "{event.price.unvalid}")
    private double price;


    public Event(String name, String description, List<String> speakers, Room room,
                 LocalDateTime eventDateTime, int projectorCode, int projectorCheck, double price) {
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
    }
}