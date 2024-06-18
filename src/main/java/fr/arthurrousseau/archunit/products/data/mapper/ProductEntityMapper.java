package fr.arthurrousseau.archunit.products.data.mapper;

import fr.arthurrousseau.archunit.products.data.model.ProductEntity;
import fr.arthurrousseau.archunit.products.service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductEntityMapper {

    ProductEntityMapper INSTANCE = Mappers.getMapper(ProductEntityMapper.class);

    Product fromEntity(ProductEntity productEntity);

    ProductEntity toEntity(Product product);
}
