package com.coresaken.memApp.service.post;

import com.coresaken.memApp.data.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class PostFileService {
    private static final List<String> ALLOWED_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final long MAX_FILE_SIZE = 4 * 1024 * 1024;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/post/";

    public static ResponseEntity<Response> upload(LocalDateTime time, MultipartFile file) {
        if (file.isEmpty()) {
            return Response.badRequest(201, "Akceptujemy jedynie rozszerzenia jpeg, png, gif i webp");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            return Response.badRequest(202, "Akceptujemy jedynie rozszerzenia jpeg, png, gif i webp");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return Response.badRequest(203, "Rozmiar pliku jest za duży");
        }

        String uuid = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String newFilename = uuid + fileExtension;

        String folderPath = getFolderPath(time);
        String finalPath = UPLOAD_DIR + folderPath;

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

        return Response.ok("/uploads/post/" +folderPath+ newFilename);
    }

    public static void remove(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName.split("/")[3]).normalize();
            File file = filePath.toFile();

            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getFolderPath(LocalDateTime time) {
        return "/" + time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";
    }
}
