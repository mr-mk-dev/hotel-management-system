package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.entity.ImageResource;
import com.hotel_management.Hotel.entity.Room;
import com.hotel_management.Hotel.repository.RoomRepo;
import com.mongodb.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepo roomRepo;
    private final CloudinaryService cloudinaryService;

    public RoomService(RoomRepo roomRepo, CloudinaryService cloudinaryService) {
        this.roomRepo = roomRepo;
        this.cloudinaryService = cloudinaryService;
    }

    public Room addRoom(String roomNo,
                        String type,
                        double pricePerNight,
                        MultipartFile[] images) throws IOException {

        if (roomRepo.findByRoomNo(roomNo).isPresent()) {
            throw new RuntimeException("Room number already exists: " + roomNo);
        }

        List<ImageResource> imageResources = new ArrayList<>();
        List<String> uploadedPublicIdsForCleanup = new ArrayList<>();

        try {
            if (images != null && images.length > 0) {
                List<CloudinaryService.UploadResult> uploaded = cloudinaryService.uploadMany(images);
                for (CloudinaryService.UploadResult r : uploaded) {
                    ImageResource ir = new ImageResource();
                    ir.setUrl(r.url());
                    ir.setPublicId(r.publicId());
                    imageResources.add(ir);
                    uploadedPublicIdsForCleanup.add(r.publicId());
                }
            }

            Room room = new Room();
            room.setRoomNo(roomNo);
            room.setType(type);
            room.setPricePerNight(pricePerNight);
            room.setImages(imageResources);

            return roomRepo.save(room);

        } catch (Exception e) {
            // cleanup newly uploaded images to avoid orphaned files
            for (String pubId : uploadedPublicIdsForCleanup) {
                try {
                    cloudinaryService.deleteByPublicId(pubId);
                } catch (Exception ignored) { /* log if needed */ }
            }

            if (e instanceof DuplicateKeyException) throw (DuplicateKeyException) e;
            throw e;
        }
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

    public Room updateMetadata(String roomNo, String type, Double pricePerNight) {
        Optional<Room> maybe = roomRepo.findByRoomNo(roomNo);
        if (maybe.isEmpty()) return null;

        Room dbRoom = maybe.get();
        if (type != null) dbRoom.setType(type);
        if (pricePerNight != null) dbRoom.setPricePerNight(pricePerNight);

        return roomRepo.save(dbRoom);
    }

    // --- Upload new images ---
    public Room addImages(String roomNo, MultipartFile[] newImages) throws IOException {
        Optional<Room> maybe = roomRepo.findByRoomNo(roomNo);
        if (maybe.isEmpty()) return null;

        Room dbRoom = maybe.get();
        List<ImageResource> newResources = new ArrayList<>();

        List<CloudinaryService.UploadResult> uploaded = cloudinaryService.uploadMany(newImages);
        for (CloudinaryService.UploadResult r : uploaded) {
            ImageResource ir = new ImageResource();
            ir.setUrl(r.url());
            ir.setPublicId(r.publicId());
            newResources.add(ir);
        }
        dbRoom.getImages().addAll(newResources);
        return roomRepo.save(dbRoom);
    }

    // --- Delete images ---
    public Room removeImages(String roomNo, List<String> publicIds) {
        Optional<Room> maybe = roomRepo.findByRoomNo(roomNo);
        if (maybe.isEmpty()) return null;

        Room dbRoom = maybe.get();

        // Filter DB images
        List<ImageResource> remaining = dbRoom.getImages().stream()
                .filter(img -> !publicIds.contains(img.getPublicId()))
                .toList();

        // Delete from Cloudinary
        for (String pubId : publicIds) {
            try {
                cloudinaryService.deleteByPublicId(pubId);
            } catch (Exception ignored) {
                System.out.printf("Failed to remove image for room %s\n", roomNo);
            }
        }

        dbRoom.setImages(remaining);
        return roomRepo.save(dbRoom);
    }


}
