package fr.arthurrousseau.archunit.products;

import fr.arthurrousseau.archunit.products.controller.mapper.ProductDtoMapper;
import fr.arthurrousseau.archunit.products.controller.model.ProductDto;
import fr.arthurrousseau.archunit.products.service.ProductService;
import fr.arthurrousseau.archunit.products.service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductControler {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        var products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") long id) {
        var product = productService.getProductById(id);

        return product.map(value -> ResponseEntity.ok(ProductDtoMapper.INSTANCE.toDto(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product add(@RequestBody Product product) {
        return productService.saveProduct(product);
    }
}
