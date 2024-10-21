package walkonmoon.fashion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    void deleteByProductId( int productId);
    void findImageByProductId(int productId);
}
