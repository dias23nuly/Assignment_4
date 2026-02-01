package repository.interfaces;

import model.Category;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
}
