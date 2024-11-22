package walkonmoon.fashion.controller;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import walkonmoon.fashion.config.FirebaseConfig;
import walkonmoon.fashion.model.Blog;
import walkonmoon.fashion.service.BlogService;
import walkonmoon.fashion.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ImageController {

    private final ImageService imageService;
    private final BlogService blogService;
    private final FirebaseConfig firebaseConfig;


    @Autowired
    public ImageController(ImageService imageService, BlogService blogService, FirebaseConfig firebaseConfig) {
        this.imageService = imageService;
        this.blogService = blogService;
        this.firebaseConfig = firebaseConfig;
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImageById(@PathVariable int id) {
        imageService.deleteImagesbyId(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            String fileName = extractFileName(fileUrl);
            BlobId blobId = BlobId.of(firebaseConfig.getBucketName(), fileName);
            boolean deleted = StorageClient.getInstance().bucket().getStorage().delete(blobId);
            if (deleted) {
                return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try(InputStream inputStream = file.getInputStream()) {
            String fileUrl = getFileName(file, inputStream);
            Map<String, String> response = new HashMap<>();
            response.put("link", fileUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Image upload failed"));
        }
    }

    @DeleteMapping("/BlogImage/{id}")
    public ResponseEntity<Void> deleteBlogImage(@PathVariable int id) {
        Blog blog =  blogService.getBlogById(id);
        blog.setThumbnail(null);
        blogService.saveBlog(blog);
        return ResponseEntity.noContent().build();
    }

    private String getFileName(MultipartFile file, InputStream inputStream) {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Blob blob = StorageClient.getInstance().bucket().create(fileName, inputStream, file.getContentType());
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", firebaseConfig.getBucketName(), fileName);
    }
    private String extractFileName(String fileUrl) {
        String[] parts = fileUrl.split("/");
        String fileName = parts[parts.length - 1].split("\\?")[0];
        return fileName;
    }
}
