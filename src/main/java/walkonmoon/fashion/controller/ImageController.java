package walkonmoon.fashion.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import walkonmoon.fashion.service.ImageService;

@RestController
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<Void> deleteImageById(@PathVariable int id) {
        imageService.deleteImagesbyId(id);
        return ResponseEntity.noContent().build();
    }
}
