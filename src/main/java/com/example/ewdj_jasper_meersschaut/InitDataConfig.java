package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import domain.Room;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import repository.EventRepository;
import repository.RoomRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class InitDataConfig implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        Room room1 = new Room("A101", 12);
        Room room2 = new Room("B202", 50);
        Room room3 = new Room("C303", 50);
        Room room4 = new Room("D404", 45);

        roomRepository.saveAll(List.of(room1, room2, room3, room4));

        Event event1 = new Event(
                "AI Workshop",
                "A hands-on workshop on AI and machine learning",
                List.of("Alice Brown", "Bob White"),
                room1,
                LocalDateTime.of(2025, 6, 15, 9, 0),
                2345,
                2345 % 97,
                59
        );

        Event event2 = new Event(
                "Cybersecurity Summit",
                "Exploring the latest trends in cybersecurity",
                List.of("Charlie Green", "Diana Black", "Kanye West"),
                room2,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                3456,
                3456 % 97,
                79
        );

        Event event3 = new Event(
                "Cloud Computing Conference",
                "Learn about advancements in cloud technologies",
                List.of("Eve Blue", "Frank Yellow"),
                room3,
                LocalDateTime.of(2025, 6, 20, 11, 0),
                4567,
                4567 % 97,
                99
        );

        Event event4 = new Event(
                "Web Development Bootcamp",
                "Master modern web development techniques",
                List.of("Grace Red", "Hank Purple"),
                room4,
                LocalDateTime.of(2025, 6, 25, 16, 0),
                5678,
                5678 % 97,
                49
        );

        eventRepository.saveAll(List.of(event1, event2, event3, event4));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Set.of("ROLE_ADMIN"));
        admin.setEnabled(true);

        User nameUser = new User();
        nameUser.setUsername("nameUser");
        nameUser.setEmail("nameuser@example.com");
        nameUser.setPassword(passwordEncoder.encode("12345678"));
        nameUser.setRoles(Set.of("ROLE_USER"));
        nameUser.setFavourites(Set.of(event3, event4));
        nameUser.setEnabled(true);

        userRepository.saveAll(List.of(admin, nameUser));
    }
}