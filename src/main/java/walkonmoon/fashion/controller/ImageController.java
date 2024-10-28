package walkonmoon.fashion.controller;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import walkonmoon.fashion.config.FirebaseConfig;
import walkonmoon.fashion.service.ImageService;

@RestController
public class ImageController {

    private final ImageService imageService;
    private final FirebaseConfig firebaseConfig;


    @Autowired
    public ImageController(ImageService imageService, FirebaseConfig firebaseConfig) {
        this.imageService = imageService;
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

    private String extractFileName(String fileUrl) {
        String[] parts = fileUrl.split("/");
        String fileName = parts[parts.length - 1].split("\\?")[0];
        return fileName;
    }
}
