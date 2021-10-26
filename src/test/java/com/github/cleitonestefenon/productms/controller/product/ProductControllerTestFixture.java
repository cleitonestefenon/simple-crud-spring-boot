package com.github.cleitonestefenon.productms.controller.product;

import com.github.cleitonestefenon.productms.product.domain.model.Product;

import java.math.BigDecimal;

public class ProductControllerTestFixture {

    public static Product mockedProduct() {
        Product mockedProduct = new Product();
        mockedProduct.setName("Mock product");
        mockedProduct.setDescription("Description mock product");
        mockedProduct.setPrice(BigDecimal.ONE);
        return mockedProduct;
    }
}
