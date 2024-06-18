package fr.arthurrousseau.archunit.products.data;

import fr.arthurrousseau.archunit.products.data.jpa.ProjectCrudRepository;
import fr.arthurrousseau.archunit.products.data.mapper.ProductEntityMapper;
import fr.arthurrousseau.archunit.products.data.model.ProductEntity;
import fr.arthurrousseau.archunit.products.service.model.Product;
import fr.arthurrousseau.archunit.products.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProjectCrudRepository projectCrudRepository;
    private final List<ProductEntity> entities = new ArrayList<>();

    @Override
    public Product save(Product product) {
        var entity = ProductEntityMapper.INSTANCE.toEntity(product);
        entities.add(entity);

        projectCrudRepository.save(entity);

        return ProductEntityMapper.INSTANCE.fromEntity(entity);
    }

    @Override
    public Optional<Product> findById(long id) {
        var p = projectCrudRepository.findById(id);
        if (p.isPresent()) {
            return Optional.of(ProductEntityMapper.INSTANCE.fromEntity(p.get()));
        }

        var product = entities.stream().filter(it -> it.getId() == id).findFirst().orElse(null);
        if (product == null) {
            return Optional.empty();
        }

        return Optional.of(ProductEntityMapper.INSTANCE.fromEntity(product));
    }

    @Override
    public List<Product> findAll() {
        return Streamable.of(projectCrudRepository.findAll()).stream().map(ProductEntityMapper.INSTANCE::fromEntity).toList();
    }

    @Override
    public boolean deleteById(long id) {
        return entities.removeIf(it -> it.getId() == id);
    }
}
