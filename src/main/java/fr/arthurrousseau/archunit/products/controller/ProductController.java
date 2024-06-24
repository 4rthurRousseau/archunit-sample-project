package fr.arthurrousseau.archunit.products.controller;

import fr.arthurrousseau.archunit.products.controller.mapper.ProductDtoMapper;
import fr.arthurrousseau.archunit.products.controller.model.ProductDto;
import fr.arthurrousseau.archunit.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        var products = productService.getAllProducts().stream()
                .map(ProductDtoMapper.INSTANCE::toDto).collect(Collectors.toList());

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
    public ProductDto add(@RequestBody ProductDto product) {
        var input = ProductDtoMapper.INSTANCE.fromDto(product);
        return ProductDtoMapper.INSTANCE.toDto(productService.saveProduct(input));
    }
}
