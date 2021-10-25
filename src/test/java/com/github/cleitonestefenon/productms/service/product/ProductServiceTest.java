package com.github.cleitonestefenon.productms.service.product;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import com.github.cleitonestefenon.productms.product.domain.repository.ProductRepository;
import com.github.cleitonestefenon.productms.product.domain.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.cleitonestefenon.productms.controller.product.ProductControllerTestFixture.mockedProduct;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductService subject;
    private ProductRepository productRepository;


    @BeforeEach
    public void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        subject = new ProductService(productRepository);
    }


    @Test
    public void shouldCreateOneProductSuccesfully() {
        Product product = mockedProduct();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = subject.createProduct(product);
        Assertions.assertNotNull(createdProduct);
        assertEqualsProperties(product, createdProduct);

        verify(productRepository, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    public void shouldUpdateOneProductSuccesfully() {

        Product product = mockedProduct();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = subject.updateProduct(product);

        Assertions.assertNotNull(updatedProduct);
        assertEqualsProperties(product, updatedProduct);

        verify(productRepository, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    public void shouldDeleteOneAddressSuccesfully() {
        doNothing().when(productRepository).delete(any(Product.class));

        Product product = mockedProduct();
        product.setId(UUID.randomUUID());

        subject.delete(product);

        verify(productRepository, times(1)).delete(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    public void shouldGetOneProductSuccesfully() {
        Optional<Product> product = Optional.of(mockedProduct());

        given(productRepository.findById(any(UUID.class))).willReturn(product);

        Optional<Product> findProductById = subject.findById(UUID.randomUUID());
        Assertions.assertTrue(findProductById.isPresent());

        assertEqualsProperties(product.get(), findProductById.get());

        verify(productRepository).findById(any(UUID.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void shouldGetAllProductSuccesfully() {
        List<Product> products = new ArrayList<>();
        products.add(mockedProduct());
        products.add(mockedProduct());

        given(productRepository.findAll()).willReturn(products);

        List<Product> findAllProducts = subject.getAllProducts();
        Assertions.assertNotNull(findAllProducts);
        Assertions.assertEquals(2, findAllProducts.size());

        assertEqualsProperties(products.get(0), findAllProducts.get(0));
        assertEqualsProperties(products.get(1), findAllProducts.get(1));

        verify(productRepository, times(1)).findAll();
        verifyNoMoreInteractions(productRepository);

    }

    private void assertEqualsProperties(Product product, Product actual) {
        Assertions.assertEquals(product.getName(), actual.getName());
        Assertions.assertEquals(product.getDescription(), actual.getDescription());
        Assertions.assertEquals(product.getPrice(), actual.getPrice());

    }

}
