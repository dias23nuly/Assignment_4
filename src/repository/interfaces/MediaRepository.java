package repository.interfaces;

import model.MediaContentBase;
import model.MediaType;

import java.util.Optional;

public interface MediaRepository extends CrudRepository<MediaContentBase> {
    boolean existsByNameAndType(String name, MediaType type);
    Optional<MediaContentBase> findByNameAndType(String name, MediaType type);
}
