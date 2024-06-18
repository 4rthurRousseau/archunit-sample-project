package fr.arthurrousseau.archunit.products.controller.mapper;

import fr.arthurrousseau.archunit.products.controller.model.ProductDto;
import fr.arthurrousseau.archunit.products.service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductDtoMapper {

    ProductDtoMapper INSTANCE = Mappers.getMapper(ProductDtoMapper.class);

    Product fromDto(ProductDto productDto);

    ProductDto toDto(Product product);
}
