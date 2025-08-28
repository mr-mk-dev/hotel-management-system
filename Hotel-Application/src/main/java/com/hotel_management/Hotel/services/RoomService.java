package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.dto.RoomUpdate;
import com.hotel_management.Hotel.entity.Room;
import com.hotel_management.Hotel.repository.RoomRepo;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepo roomRepo;

    public Room addRoom(Room room){
        return roomRepo.save(room);
    }

    public Room removeRoom(String roomNo){
        return roomRepo.deleteByRoomNo(roomNo).orElse(null);
    }

    public List<Room> roomList (){
        return roomRepo.findAll();
    }

    public List<Room> findByRoomType(String type){
        return roomRepo.findByType(type).orElse(null);
    }

    public Room findByRoomNo(String roomNo){
        return roomRepo.findByRoomNo(roomNo).orElse(null);
    }

    public List<Room> findByPricePerNightGreaterThan(double amount){
        return roomRepo.findByPricePerNightGreaterThan(amount).orElse(null);
    }

    public List<Room> findByPricePerNightLessThan(double amount){
        return roomRepo.findByPricePerNightLessThan(amount).orElse(null);
    }

    public List<Room> findByPricePerNightBetween(double min,double max){
        return roomRepo.findByPricePerNightBetween(min,max).orElse(null);
    }

    public Room updateRoom(String roomNo , RoomUpdate roomUpdate){
        Room dbRoom = roomRepo.findByRoomNo(roomNo).orElse(null);
        if(dbRoom!=null){
            dbRoom.setType(roomUpdate.getType());
            dbRoom.setPricePerNight(roomUpdate.getAmount());
            return roomRepo.save(dbRoom);
        }
        return null;
    }


}
