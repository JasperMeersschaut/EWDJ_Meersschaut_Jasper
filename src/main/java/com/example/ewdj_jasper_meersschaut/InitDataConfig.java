package com.example.ewdj_jasper_meersschaut;

import domain.Event;
import domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repository.EventRepository;
import repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitDataConfig implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(String... args) {
        // Save the Room first
        Room room = new Room("A101", 100);
        roomRepository.save(room); // Ensure the Room is saved in the database

        // Save the Event with the persisted Room
        eventRepository.save(new Event(
                "Tech Conference",
                "A conference about the latest in tech",
                List.of("John Doe", "Jane Smith", "Alice Johnson"),
                room, // Use the persisted Room
                LocalDateTime.of(2025, 5, 20, 10, 0),
                1234,
                56,
                49
        ));
    }}
