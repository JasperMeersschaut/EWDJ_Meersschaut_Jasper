//package service;
//
//import domain.Room;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import repository.RoomRepository;
//
//@Service
//public class RoomService {
//    @Autowired
//    private RoomRepository roomRepository;
//
//    public Room saveRoom(Room room) {
//        if (roomRepository.existsByName(room.getName())) {
//            throw new IllegalArgumentException("Room name already exists.");
//        }
//        return roomRepository.save(room);
//    }
//}