package fr.arthurrousseau.archunit.products.service;

import fr.arthurrousseau.archunit.products.service.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product saveProduct(Product product);

    Optional<Product> getProductById(long id);

    List<Product> getAllProducts();

    boolean deleteProduct(long id);
}
