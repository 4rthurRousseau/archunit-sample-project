package fr.arthurrousseau.archunit.products.service.impl;

import fr.arthurrousseau.archunit.products.service.ProductService;
import fr.arthurrousseau.archunit.products.service.model.Product;
import fr.arthurrousseau.archunit.products.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Products implements ProductService {

    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        log.info("Demande d'enregistrement d'un produit " + product);
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean deleteProduct(long id) {
        return productRepository.deleteById(id);
    }
}
