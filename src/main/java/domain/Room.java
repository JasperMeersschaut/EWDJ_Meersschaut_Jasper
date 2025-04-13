package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{room.name.required}")
    @Pattern(regexp = "^[A-Za-z]\\d{3}$", message = "{room.name.invalid}")
    private String name;

    @Min(value = 1, message = "{room.capacity.invalid}")
    @Max(value = 505, message = "{room.capacity.invalid}")
    private int capacity;

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }
}