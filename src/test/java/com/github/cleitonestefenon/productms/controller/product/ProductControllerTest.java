package com.github.cleitonestefenon.productms.controller.product;

import com.github.cleitonestefenon.productms.product.domain.model.Product;
import com.github.cleitonestefenon.productms.product.domain.service.ProductService;
import com.github.cleitonestefenon.productms.product.interfaces.dto.ProductDto;
import com.github.cleitonestefenon.productms.product.interfaces.rest.ProductController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.cleitonestefenon.productms.controller.product.ProductControllerTestFixture.mockedProduct;
import static com.github.cleitonestefenon.productms.controller.product.ProductServiceTestFixture.mockedProductDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class ProductControllerTest {

    private ProductController subject;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = Mockito.mock(ProductService.class);
        subject = new ProductController(productService);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void shouldCreateOneProductSuccesfully() {
        Product product = mockedProduct();

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<?> createdProductDto = subject.createProduct(mockedProductDto());

        Assertions.assertNotNull(createdProductDto);
        Assertions.assertEquals(201, createdProductDto.getStatusCode().value());
        Assertions.assertNotNull(createdProductDto.getBody());

        verify(productService, times(1)).createProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldUpdateOneProductSuccesfully() {
        Optional<Product> product = Optional.of(mockedProduct());

        when(productService.findById(any(UUID.class))).thenReturn(product);
        when(productService.updateProduct(any(Product.class))).thenReturn(product.get());

        ResponseEntity<?> updatedProductDto = subject.updateProduct(mockedProductDto(), UUID.randomUUID());

        Assertions.assertNotNull(updatedProductDto);
        Assertions.assertNotNull(updatedProductDto.getBody());
        Assertions.assertEquals(HttpStatus.OK, updatedProductDto.getStatusCode());

        verify(productService).findById(any(UUID.class));
        verify(productService).updateProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldGetOneProductSuccesfully() {
        Optional<Product> product = Optional.of(mockedProduct());

        when(productService.findById(any(UUID.class))).thenReturn(product);

        ResponseEntity<?> productDto = subject.getProduct(UUID.randomUUID());
        Assertions.assertNotNull(productDto);
        Assertions.assertNotNull(productDto.getBody());
        Assertions.assertEquals(HttpStatus.OK, productDto.getStatusCode());

        verify(productService, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldGetAllProductSuccesfully() {
        List<Product> products = new ArrayList<>();
        products.add(mockedProduct());
        products.add(mockedProduct());

        given(productService.getAllProducts()).willReturn(products);

        ResponseEntity<List<ProductDto>> findAllProductDto = subject.getAllProducts();
        Assertions.assertNotNull(findAllProductDto);
        Assertions.assertNotNull(findAllProductDto.getBody());
        Assertions.assertEquals(HttpStatus.OK, findAllProductDto.getStatusCode());
        Assertions.assertEquals(2, findAllProductDto.getBody().size());

        assertEqualsProperties(products.get(0), findAllProductDto.getBody().get(0));
        assertEqualsProperties(products.get(1), findAllProductDto.getBody().get(1));

        verify(productService, times(1)).getAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldDeleteOneAddressSuccesfully() {
        Optional<Product> product = Optional.of(mockedProduct());
        product.get().setId(UUID.randomUUID());

        when(productService.findById(any(UUID.class))).thenReturn(product);
        doNothing().when(productService).delete(product.get());

        ResponseEntity<?> deleteProductDto = subject.deleteProduct(UUID.randomUUID());
        Assertions.assertNotNull(deleteProductDto);
        Assertions.assertNull(deleteProductDto.getBody());
        Assertions.assertEquals(HttpStatus.OK, deleteProductDto.getStatusCode());

        verify(productService, times(1)).findById(any(UUID.class));
        verify(productService, times(1)).delete(any(Product.class));
        verifyNoMoreInteractions(productService);
    }


    private void assertEqualsProperties(Product product, ProductDto productDto) {
        Assertions.assertEquals(product.getName(), productDto.getName());
        Assertions.assertEquals(product.getDescription(), productDto.getDescription());
        Assertions.assertEquals(product.getPrice(), productDto.getPrice());
    }

}
