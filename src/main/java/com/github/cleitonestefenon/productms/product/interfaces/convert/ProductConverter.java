package com.github.cleitonestefenon.productms.product.interfaces.convert;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import com.github.cleitonestefenon.productms.product.interfaces.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public abstract class ProductConverter {

    public abstract Product createProduct(ProductDto productDto);

    public abstract ProductDto createProductDto(Product product);

    public abstract Product updateProduct(ProductDto productDto, @MappingTarget Product product);

}
