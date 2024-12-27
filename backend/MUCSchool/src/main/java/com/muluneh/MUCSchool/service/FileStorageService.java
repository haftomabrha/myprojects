package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.User;
import com.muluneh.MUCSchool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileStorageService {
    @Autowired
    private UserRepository userRepository;
    private final String uploadDirectory = "uploads/";
    public String storeFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/" + fileName;  // Return relative path for serving
    }
}
