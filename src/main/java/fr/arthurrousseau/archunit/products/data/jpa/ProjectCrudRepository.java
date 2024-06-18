package fr.arthurrousseau.archunit.products.data.jpa;

import fr.arthurrousseau.archunit.products.data.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProjectCrudRepository extends CrudRepository<ProductEntity, Long> {
}
