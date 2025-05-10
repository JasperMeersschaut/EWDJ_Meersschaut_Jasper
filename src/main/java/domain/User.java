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
@ToString(exclude = {"id", "favourites", "roles"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "{user.username.required}")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "{user.email.required}")
    @Email(message = "{user.email.invalid}")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "{user.password.required}")
    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @Size(max = 5, message = "{user.favorites.max}")
    private Set<Event> favourites = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    )
    @Column(name = "authority", nullable = false)
    private Set<String> roles = new HashSet<>();

    @Column(nullable = false)
    private boolean enabled = true;
}