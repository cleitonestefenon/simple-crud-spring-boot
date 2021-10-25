package com.github.cleitonestefenon.productms.product.domain.service;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import com.github.cleitonestefenon.productms.product.domain.repository.ProductCustomRepository;
import com.github.cleitonestefenon.productms.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCustomRepository productCustomRepository;

    public ProductService(ProductRepository productRepository,
                          ProductCustomRepository productCustomRepository) {
        this.productRepository = productRepository;
        this.productCustomRepository = productCustomRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String param, BigDecimal minPrice, BigDecimal maxPrice) {
        return productCustomRepository.searchProduct(param, minPrice, maxPrice);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }
}
