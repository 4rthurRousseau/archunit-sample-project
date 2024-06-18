package fr.arthurrousseau.archunit.products.service.repository;

import fr.arthurrousseau.archunit.products.service.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(long id);

    List<Product> findAll();

    boolean deleteById(long id);
}
