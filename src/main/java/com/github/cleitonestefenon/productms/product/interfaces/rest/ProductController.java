package com.github.cleitonestefenon.productms.product.interfaces.rest;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import com.github.cleitonestefenon.productms.product.domain.repository.ProductCustomRepository;
import com.github.cleitonestefenon.productms.product.domain.service.ProductService;
import com.github.cleitonestefenon.productms.product.interfaces.convert.ProductConverter;
import com.github.cleitonestefenon.productms.product.interfaces.dto.ProductDto;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto productDto) {
        Product product = productService.createProduct(productConverter.createProduct(productDto));
        return new ResponseEntity<>(productConverter.createProductDto(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable UUID id) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productDto.setId(id);
        return new ResponseEntity<>(
                productService.updateProduct(productConverter.updateProduct(productDto, product.get())),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable UUID id) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productConverter.createProductDto(product.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts()
                .stream()
                .map(productConverter::createProductDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam(value = "q", required = false) String param,
                                            @RequestParam(value = "min_price", required = false) BigDecimal minPrice,
                                            @RequestParam(value = "max_price", required = false) BigDecimal maxPrice) {

        List<Product> products = productService.searchProducts(param, minPrice, maxPrice);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.delete(product.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

