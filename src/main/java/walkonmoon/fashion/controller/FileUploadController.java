package walkonmoon.fashion.controller;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import walkonmoon.fashion.config.FirebaseConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class FileUploadController {
    private final FirebaseConfig firebaseConfig;

    public FileUploadController(FirebaseConfig firebaseConfig) {
        this.firebaseConfig = firebaseConfig;
    }

    // Single file upload
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        try (InputStream inputStream = file.getInputStream()) {
            String fileUrl = getFileName(file, inputStream);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Multiple file upload
    @PostMapping("/uploadMultiple")
    public ResponseEntity<String> handleMultipleFileUpload(@RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return new ResponseEntity<>("Files are empty", HttpStatus.BAD_REQUEST);
        }

        StringBuilder fileUrls = new StringBuilder();
        for (MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                String fileUrl = getFileName(file, inputStream);
                fileUrls.append(fileUrl).append("\n");
            } catch (IOException e) {
                return new ResponseEntity<>("Failed to upload files", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(fileUrls.toString(), HttpStatus.OK);
    }

    private String getFileName(MultipartFile file, InputStream inputStream) {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Blob blob = StorageClient.getInstance().bucket().create(fileName, inputStream, file.getContentType());
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", firebaseConfig.getBucketName(), fileName);
    }

    // Method to delete a file based on URL
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
//        try {
//            String fileName = extractFileName(fileUrl);
//            BlobId blobId = BlobId.of(firebaseConfig.getBucketName(), fileName);
//            boolean deleted = StorageClient.getInstance().bucket().getStorage().delete(blobId);
//            if (deleted) {
//                return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Helper method to extract file name from URL
//    private String extractFileName(String fileUrl) {
//        String[] parts = fileUrl.split("/");
//        String fileName = parts[parts.length - 1].split("\\?")[0];
//        return fileName;
//    }

}