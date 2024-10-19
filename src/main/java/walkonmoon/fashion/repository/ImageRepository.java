package walkonmoon.fashion.repository;

import org.springframework.data.repository.CrudRepository;
import walkonmoon.fashion.model.Image;

public interface ImageRepository extends CrudRepository<Image, Integer> {
}
