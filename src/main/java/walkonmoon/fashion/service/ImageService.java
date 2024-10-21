package walkonmoon.fashion.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.model.Product;
import walkonmoon.fashion.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getListImages() {
        return (List<Image>) imageRepository.findAll();
    }

    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    public Image getImageById(int id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.orElse(null);
    }

    public void deleteByProductId(int productId) {
        for (Image image : imageRepository.findAll()) {
            if (image.getProductId() == productId) {
                imageRepository.delete(image);
            }
        }
    }

    public List<Image> findByProductId(int productId) {
        return imageRepository.findByProductId(productId);
    }

        public List<Image> findImageByProductId(int productId){
            List<Image> imageList = new ArrayList<>();
             for(Image image : imageRepository.findAll()){
                 if(image.getProductId() == productId){
                      imageList.add(image);
                 }
             }

             return imageList;
        }

        public void deleteImagesbyId(int id){
             imageRepository.deleteById(id);
        }

}
