package com.hotel_management.Hotel.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public UploadResult upload(MultipartFile file) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "hotel_rooms"   // optional: keep uploads organized
                ));
        String url = (String) result.get("secure_url");
        String publicId = (String) result.get("public_id");
        return new UploadResult(url, publicId);
    }

    public List<UploadResult> uploadMany(MultipartFile[] files) throws IOException {
        List<UploadResult> list = new ArrayList<>();
        for (MultipartFile f : files) {
            list.add(upload(f));
        }
        return list;
    }

    public void deleteByPublicId(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public static record UploadResult(String url, String publicId) {}
}

