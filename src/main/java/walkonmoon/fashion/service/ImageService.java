package walkonmoon.fashion.service;

import org.springframework.stereotype.Service;
import walkonmoon.fashion.model.Image;
import walkonmoon.fashion.repository.ImageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

     public ImageService(ImageRepository imageRepository) {
         this.imageRepository = imageRepository;
     }

      public List<Image> getListImages(){
         return (List<Image>) imageRepository.findAll();
      }

       public void saveImage(Image image){
          imageRepository.save(image);
       }

        public Image getImageById(int id){
          Optional<Image> image = imageRepository.findById(id);
          return image.orElse(null);
        }

}
