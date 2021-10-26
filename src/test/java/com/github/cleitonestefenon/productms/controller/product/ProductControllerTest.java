package com.github.cleitonestefenon.productms.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cleitonestefenon.productms.product.domain.model.Product;
import com.github.cleitonestefenon.productms.product.domain.service.ProductService;
import com.github.cleitonestefenon.productms.product.interfaces.dto.ProductDto;
import com.github.cleitonestefenon.productms.product.interfaces.rest.ProductController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.github.cleitonestefenon.productms.controller.product.ProductControllerTestFixture.mockedProduct;
import static com.github.cleitonestefenon.productms.controller.product.ProductServiceTestFixture.mockedProductDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ProductControllerTest {

    private ProductController subject;
    private ProductService productService;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        productService = Mockito.mock(ProductService.class);
        subject = new ProductController(productService);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        mvc = MockMvcBuilders.standaloneSetup(subject).build();
    }

    @Test
    public void shouldCreateOneProductSuccessfully() {
        when(productService.createProduct(any(Product.class))).thenReturn(mockedProduct());

        ResponseEntity<?> createdProductDto = subject.createProduct(mockedProductDto());
        Assertions.assertNotNull(createdProductDto);
        Assertions.assertEquals(HttpStatus.CREATED, createdProductDto.getStatusCode());
        Assertions.assertNotNull(createdProductDto.getBody());

        verify(productService, times(1)).createProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldReturnExceptionWhenCreateProductWithNegativeValue() throws Exception {
        ProductDto productDto = mockedProductDto();
        productDto.setPrice(BigDecimal.valueOf(-5));

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(new ObjectMapper().writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertTrue(assertContainsErrorMessage(result, "price needs to be positive"));
        verify(productService, times(0)).createProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldReturnExceptionWhenCreateProductWithNullValue() throws Exception {
        ProductDto productDto = mockedProductDto();
        productDto.setName(null);

        MvcResult result = this.mvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(new ObjectMapper().writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertTrue(assertContainsErrorMessage(result, "product name is required"));
        verify(productService, times(0)).createProduct(any(Product.class));
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

        verify(productService, times(1)).findById(any(UUID.class));
        verify(productService, times(1)).updateProduct(any(Product.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingProductWithMissingProduct() {
        when(productService.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> productNotFound = subject.updateProduct(mockedProductDto(), UUID.randomUUID());
        assertNotNull(productNotFound);
        assertNull(productNotFound.getBody());
        assertEquals(HttpStatus.NOT_FOUND, productNotFound.getStatusCode());

        verify(productService, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void shouldGetOneProductSuccessfully() {
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
    public void shouldReturnNotFoundWhenSearchProduct() {
        when(productService.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> productNotFound = subject.getProduct(UUID.randomUUID());
        assertNotNull(productNotFound);
        assertNull(productNotFound.getBody());
        assertEquals(HttpStatus.NOT_FOUND, productNotFound.getStatusCode());

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

    @Test
    public void shouldReturnNotFoundwhenDeleteProductThatDoesntExist() {
        when(productService.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseEntity<?> productNotFound = subject.getProduct(UUID.randomUUID());
        assertNotNull(productNotFound);
        assertNull(productNotFound.getBody());
        assertEquals(HttpStatus.NOT_FOUND, productNotFound.getStatusCode());

        verify(productService, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(productService);
    }

    private Boolean assertContainsErrorMessage(MvcResult result, String message) {
        return Objects.requireNonNull(result.getResolvedException()).getMessage().contains(message);
    }


    private void assertEqualsProperties(Product product, ProductDto productDto) {
        Assertions.assertEquals(product.getName(), productDto.getName());
        Assertions.assertEquals(product.getDescription(), productDto.getDescription());
        Assertions.assertEquals(product.getPrice(), productDto.getPrice());
    }

}
