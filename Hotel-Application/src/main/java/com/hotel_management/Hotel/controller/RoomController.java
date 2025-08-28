package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.dto.RoomUpdate;
import com.hotel_management.Hotel.entity.Room;
import com.hotel_management.Hotel.services.RoomService;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> roomList() {
        return ResponseEntity.ok(roomService.roomList());
    }

    @GetMapping("/find-type/{type}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByRoomType(@PathVariable String type) {
        return ResponseEntity.ok(roomService.findByRoomType(type));
    }

    @GetMapping("/find-room/{roomNo}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findByRoomNo(@PathVariable String roomNo) {
        Room room = roomService.findByRoomNo(roomNo);
        return room != null
                ? ResponseEntity.ok(room)
                : ResponseEntity.status(404).body(Map.of("error", "Room not found", "roomNo", roomNo));
    }

    @GetMapping("/find-amountgreater/{amount}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByPricePerNightGreaterThan(@PathVariable double amount) {
        return ResponseEntity.ok(roomService.findByPricePerNightGreaterThan(amount));
    }

    @GetMapping("/find-amountless/{amount}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByPricePerNightLessThan(@PathVariable double amount) {
        return ResponseEntity.ok(roomService.findByPricePerNightLessThan(amount));
    }

    @GetMapping("/find-between")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByPricePerNightBetween(@RequestParam double from, @RequestParam double to) {
// -------- Looks like ---------- localhost:8080/find-between?from=1000&to=3000 ------
        return ResponseEntity.ok(roomService.findByPricePerNightBetween(from, to));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addRoom(@Validated @RequestBody Room room) {
        try {
            return ResponseEntity.status(201).body(roomService.addRoom(room));
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(409).body(Map.of("error", "Room number already exists"));
        }
    }

    @DeleteMapping("/delete/{roomNo}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> removeRoom(@PathVariable String roomNo) {
        Room room = roomService.removeRoom(roomNo);
        return room != null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(404).body(Map.of("error", "Room not found", "roomNo", roomNo));
    }

    @PutMapping("/update/{roomNo}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> updateRoom(@PathVariable String roomNo, @RequestBody RoomUpdate roomUpdate) {
        Room updated = roomService.updateRoom(roomNo, roomUpdate);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.status(404).body(Map.of("error", "Room not found", "roomNo", roomNo));
    }
}
