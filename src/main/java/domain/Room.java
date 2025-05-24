package domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@JsonPropertyOrder({"id", "name", "capacity"})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "{room.name.required}")
    @Pattern(regexp = "^[A-Za-z]\\d{3}$", message = "{room.name.invalid}")
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "{validation.room.capacity.min}")
    @Max(value = 50, message = "{validation.room.capacity.max}")
    private int capacity;

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }


    public static class RoomFactory {
        public static Room createRoom() {
            return new Room();
        }
    }
}