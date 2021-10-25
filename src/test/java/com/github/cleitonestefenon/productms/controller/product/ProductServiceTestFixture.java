package com.github.cleitonestefenon.productms.controller.product;

import com.github.cleitonestefenon.productms.product.interfaces.dto.ProductDto;

import java.math.BigDecimal;

public class ProductServiceTestFixture {

    public static ProductDto mockedProductDto() {
        ProductDto mockedProductDto = new ProductDto();
        mockedProductDto.setName("Mock product");
        mockedProductDto.setDescription("Description mock product");
        mockedProductDto.setPrice(BigDecimal.ONE);
        return mockedProductDto;
    }
}
