package service;

import domain.Room;
import jakarta.validation.Valid;
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
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public void saveRoom(@Valid Room room) {
        roomRepository.save(room);
    }

}