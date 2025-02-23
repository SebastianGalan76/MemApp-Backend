package com.coresaken.memApp.service.user;

import com.coresaken.memApp.data.response.Response;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UserAvatarFileService {
    private final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/webp");
    private final long MAX_FILE_SIZE = 4 * 1024 * 1024;
    private final String UPLOAD_DIR = System.getProperty("user.dir");

    public ResponseEntity<Response> upload(LocalDateTime time, MultipartFile file) {
        if (file.isEmpty()) {
            return Response.badRequest(201, "Akceptujemy jedynie rozszerzenia jpeg, png i webp");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            return Response.badRequest(202, "Akceptujemy jedynie rozszerzenia jpeg, png i webp");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return Response.badRequest(203, "Rozmiar pliku jest za duży");
        }

        String uuid = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String newFilename = uuid + fileExtension;

        String folderPath = getFolderPath(time);
        String finalPath = UPLOAD_DIR + "/uploads/user/avatar/" + folderPath;

        try {
            Path uploadPath = Paths.get(finalPath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            file.transferTo(new File(uploadPath +"\\" + newFilename));
        } catch (IOException e) {
            e.printStackTrace();
            return Response.badRequest(204, "Pojawił się błąd #4124. Spróbuj ponownie później lub skontaktuj się z Administracją.");
        }

        return Response.ok("/uploads/user/avatar/" +folderPath+ newFilename);
    }

    public ResponseEntity<Resource> get(String filename) {
        File file = new File(UPLOAD_DIR +"/uploads/user/avatar/" + filename);
        if (!file.exists() || file.isDirectory()) {
            System.out.println(file.getPath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource;
        try {
            resource = new UrlResource(file.toURI());
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String contentType = determineContentType(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    public void remove(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName.split("/")[4]).normalize();
            File file = filePath.toFile();

            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getFolderPath(LocalDateTime time) {
        return "/" + time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
    }

    // Helper method to extract file extension
    private String getFileExtension(String filename) {
        int lastIndexOfDot = filename.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return filename.substring(lastIndexOfDot);
    }

    // Helper method to determine the Content-Type based on file extension
    private String determineContentType(String filename) {
        String fileExtension = getFileExtension(filename).toLowerCase();
        switch (fileExtension) {
            case ".jpeg":
            case ".jpg":
                return MediaType.IMAGE_JPEG_VALUE;
            case ".png":
                return MediaType.IMAGE_PNG_VALUE;
            case ".webp":
                return "image/webp";
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
