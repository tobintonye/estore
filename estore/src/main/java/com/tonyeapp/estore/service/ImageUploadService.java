package com.tonyeapp.estore.service;

import com.cloudinary.Cloudinary;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException { 

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        try {
            // Upload to Cloudinary and return the secure URL immediately
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return uploadResult.get("secure_url").toString();
        } catch (java.io.IOException e) {
            e.printStackTrace(); // Log the error details
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }
}
