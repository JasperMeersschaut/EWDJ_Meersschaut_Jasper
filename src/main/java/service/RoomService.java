package service;

import domain.Room;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RoomRepository;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public boolean existsByName(String name) {
        return roomRepository.existsByName(name);
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id));
    }

    public int findCapacityById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + id))
                .getCapacity();
    }

    @Transactional
    public void save(Room room) {
        roomRepository.save(room);
        System.out.println("Saved room: " + room);
    }

}