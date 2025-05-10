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
        Room room1 = new Room("A101", 100);
        Room room2 = new Room("B202", 50);
        Room room3 = new Room("C303", 200);
        Room room4 = new Room("D404", 150);

        roomRepository.saveAll(List.of(room1, room2, room3, room4));

        eventRepository.saveAll(List.of(
                new Event(
                        "AI Workshop",
                        "A hands-on workshop on AI and machine learning",
                        List.of("Alice Brown", "Bob White"),
                        room1,
                        LocalDateTime.of(2025, 6, 15, 9, 0),
                        2345,
                        78,
                        59
                ),
                new Event(
                        "Cybersecurity Summit",
                        "Exploring the latest trends in cybersecurity",
                        List.of("Charlie Green", "Diana Black"),
                        room2,
                        LocalDateTime.of(2025, 7, 10, 14, 0),
                        3456,
                        89,
                        79
                ),
                new Event(
                        "Cloud Computing Conference",
                        "Learn about advancements in cloud technologies",
                        List.of("Eve Blue", "Frank Yellow"),
                        room3,
                        LocalDateTime.of(2025, 8, 20, 11, 0),
                        4567,
                        90,
                        99
                ),
                new Event(
                        "Web Development Bootcamp",
                        "Master modern web development techniques",
                        List.of("Grace Red", "Hank Purple"),
                        room4,
                        LocalDateTime.of(2025, 9, 5, 16, 0),
                        5678,
                        67,
                        49
                )
        ));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User admin = new User();
        admin.setFirstName("admin");
        admin.setEmail("admin@example.com");
        admin.setWachtwoord(passwordEncoder.encode("$2y$10$xT2EKeAP.Ey84iy5dOwuOe5hxtRhvGVk6aLIpgAIpAzzu8xfJWPpO"));
        admin.setAdmin(true);

        User nameUser = new User();
        nameUser.setFirstName("nameUser");
        nameUser.setEmail("nameuser@example.com");
        nameUser.setWachtwoord(passwordEncoder.encode("$2y$10$E.442wS/c9QXLpkLcXaOY.Bet9jTm/aoOUi65yvtuvmJuBJJu1KcG"));
        nameUser.setAdmin(true);


        userRepository.saveAll(List.of(admin, nameUser));
    }
}