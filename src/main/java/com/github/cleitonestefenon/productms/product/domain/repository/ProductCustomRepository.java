package com.github.cleitonestefenon.productms.product.domain.repository;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class ProductCustomRepository {

    private final EntityManager entityManager;

    public ProductCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Product> searchProduct(String param, BigDecimal minPrice, BigDecimal maxPrice) {

        String stringQuery = "select p from Product as p ";
        String condicao = "where ";

        if (param != null) {
            stringQuery += condicao + "p.name like CONCAT('%',:param,'%') or " +
                    "p.description like CONCAT('%',:param,'%') ";
            condicao = "or ";
        }

        if (minPrice != null && maxPrice != null) {
            stringQuery += condicao + "p.price between :minPrice and :maxPrice ";
        } else {
            if (minPrice != null) {
                stringQuery += condicao + "p.price >= :minPrice ";
            }

            if (maxPrice != null) {
                stringQuery += condicao + "p.price <= :maxPrice ";
            }
        }

        var query = entityManager.createQuery(stringQuery, Product.class);

        if (param != null) {
            query.setParameter("param", param);
        }

        if (minPrice != null && maxPrice != null) {
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
        } else {
            if (minPrice != null) {
                query.setParameter("minPrice", minPrice);
            }

            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }
        }

        return query.getResultList();
    }
}
