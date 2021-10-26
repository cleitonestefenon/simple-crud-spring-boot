package com.github.cleitonestefenon.productms.product.interfaces.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductDto {

    private UUID id;

    @NotEmpty(message = "product name is required")
    @Size(max = 50)
    private String name;

    @NotEmpty(message = "product description is required")
    private String description;

    @DecimalMin(value = "0.00", message = "price needs to be positive")
    @NotNull(message = "product price is required")
    private BigDecimal price;
}
