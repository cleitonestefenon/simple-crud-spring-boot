package com.github.cleitonestefenon.productms.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "pro_id")
    private UUID id;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "pro_name")
    private String name;

    @NotEmpty
    @Column(name = "pro_description")
    private String description;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "pro_price")
    private BigDecimal price;

}
