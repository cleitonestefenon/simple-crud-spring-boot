package com.github.cleitonestefenon.productms.product.domain.repository;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p where " +
            ":param is not null and p.name like CONCAT('%',:param,'%') or " +
            ":param is not null and p.description like CONCAT('%',:param,'%') or " +
            ":minPrice is not null and p.price >= :minPrice or " +
            ":maxPrice is not null and p.price <= :maxPrice or " +
            ":maxPrice is not null and :minPrice is not null and p.price between :minPrice and :maxPrice")
    List<Product> searchByParams(String param, BigDecimal minPrice, BigDecimal maxPrice);
}
