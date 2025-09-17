package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.entity.Room;
import com.hotel_management.Hotel.services.RoomService;
import com.mongodb.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

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
        return room != null ? ResponseEntity.ok(room) : ResponseEntity.status(404).body(Map.of("error", "Room not found", "roomNo", roomNo));
    }

    @GetMapping("/find-priceGreater/{amount}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByPricePerNightGreaterThan(@PathVariable double amount) {
        return ResponseEntity.ok(roomService.findByPricePerNightGreaterThan(amount));
    }

    @GetMapping("/find-priceLess/{amount}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByPricePerNightLessThan(@PathVariable double amount) {
        return ResponseEntity.ok(roomService.findByPricePerNightLessThan(amount));
    }

    @GetMapping("/find-between")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Room>> findByPricePerNightBetween(
            @RequestParam double from,
            @RequestParam double to) {

    // -------- Looks like ---------- localhost:8080/find-between?from=1000&to=3000 ------
        return ResponseEntity.ok(roomService.findByPricePerNightBetween(from, to));
    }


    @DeleteMapping("/delete/{roomNo}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> removeRoom(@PathVariable String roomNo) {
        Room room = roomService.removeRoom(roomNo);
        return room != null ?ResponseEntity.ok(Map.of("message", "Booking deleted successfully"))
                : ResponseEntity.status(404)
                .body(Map.of("error", "Room not found", "roomNo", roomNo));
    }


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addRoom(
            @RequestParam String roomNo,
            @RequestParam String type,
            @RequestParam double pricePerNight,
            @RequestParam(name = "images", required = false) MultipartFile[] images
    ) {
        try {
            Room saved = roomService.addRoom(roomNo, type, pricePerNight, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Room number already exists"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add room: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{roomNo}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> updateRoomMetadata(
            @PathVariable String roomNo,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "pricePerNight", required = false) Double pricePerNight
    ) {
        Room updated = roomService.updateMetadata(roomNo, type, pricePerNight);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Room not found"));
        }
        return ResponseEntity.ok(updated);
    }

    @PostMapping(value = "/add-images/{roomNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> addRoomImages(
            @PathVariable String roomNo,
            @RequestParam("images") MultipartFile[] images
    ) {
        try {
            Room updated = roomService.addImages(roomNo, images);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Room not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload images: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove-images/{roomNo}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> deleteRoomImages(
            @PathVariable String roomNo,
            @RequestParam("publicIds") List<String> publicIds
    ) {
        try {
            Room updated = roomService.removeImages(roomNo, publicIds);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Room not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete images: " + e.getMessage()));
        }
    }

}
