package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    SecurityContext context;
    @Mock
    Authentication authentication;

    @Test
    void findAll() {
        Product product1 = new Product();
        Product product2 = new Product();
        Set<Product> products = new HashSet<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        Set<Product> productsAll = productService.findAll();

        assertNotNull(productsAll);
        assertEquals(2, productsAll.size());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));

        Product product = productService.findById(1L);

        assertNotNull(product);
        verify(productRepository).findById(anyLong());
    }

    @Test
    void save() {
        when(productRepository.save(any())).thenReturn(new Product());

        Product product = productService.save(new Product());

        assertNotNull(product);
        verify(productRepository).save(any());
    }

    @Test
    void deleteById() {
        Product product = new Product();
        User user = new User();
        user.setUsername("test");
        product.setCoach(user);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test");
        SecurityContextHolder.setContext(context);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        productService.deleteById(1L);

        verify(productRepository).deleteById(anyLong());
    }

    @Test
    void findAllByUser() {
        Product product1 = new Product();
        Product product2 = new Product();
        Set<Product> productsByUser = new HashSet<>();
        productsByUser.add(product1);
        productsByUser.add(product2);

        when(productRepository.findAllByCoach(notNull())).thenReturn(productsByUser);

        Set<Product> products = productService.findAllByUser(new User());

        assertEquals(2, products.size());
        verify(productRepository).findAllByCoach(any());
    }
}