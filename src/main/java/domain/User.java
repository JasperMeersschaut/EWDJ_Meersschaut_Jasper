package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = {"id", "favourites"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "{user.firstname.required}")
    @Column(unique = true)
    private String firstName;

    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "{user.password.required}")
    private String wachtwoord;

    // Vervanging van de String rol door een boolean admin
    @Column(nullable = false)
    private boolean isAdmin;

    @ManyToMany
    @JoinTable(
            name = "gebruiker_favorieten",
            joinColumns = @JoinColumn(name = "gebruiker_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @Size(max = 5, message = "{user.favorites.max}")
    private Set<Event> favourites = new HashSet<>();

//    public boolean addFavorite(Event event) {
//        if (favorites.size() < 5) {
//            return favorites.add(event);
//        }
//        return false;
//    }
//
//    public boolean removeFavorite(Event event) {
//        return favorites.remove(event);
//    }
}