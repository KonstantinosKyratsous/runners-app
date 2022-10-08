package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ProductRating;
import com.kyratsous.runnersapp.services.ProductService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.ProductFavoriteService;
import com.kyratsous.runnersapp.services.ratings.ProductRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.emptyArray;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService productService;
    @Mock
    UserService userService;
    @Mock
    ProductRatingService productRatingService;
    @Mock
    ProductFavoriteService productFavoriteService;

    @InjectMocks
    ProductController controller;

    MockMvc mockMvc;

    Set<Product> products;
    Product product1 = new Product();
    Product product2 = new Product();

    Set<ProductRating> ratings;
    ProductRating rating1;
    ProductRating rating2;

    Set<Long> favorites;

    @BeforeEach
    void setUp() {
        products = new HashSet<>();
        products.add(product1);
        products.add(product2);

        ratings = new HashSet<>();
        ratings.add(rating1);
        ratings.add(rating2);

        favorites = new HashSet<>();
        favorites.add(1L);
        favorites.add(2L);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getProducts() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(productService.findAll()).thenReturn(products);
        when(productFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/index"))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("favorites", favorites));
    }

    @Test
    void showProduct() throws Exception {
        when(productService.findById(anyLong())).thenReturn(product1);
        when(productRatingService.findAllByProductId(anyLong())).thenReturn(ratings);
        when(productFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/show"))
                .andExpect(model().attribute("product", product1))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("isFavorite", true));
    }

    @Test
    void getMyProducts() throws Exception {
        when(productService.findAllByUser(any())).thenReturn(products);

        mockMvc.perform(get("/my-products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/my-products"))
                .andExpect(model().attribute("products", products));
    }

    @Test
    void createProductForm() throws Exception {
        mockMvc.perform(get("/my-products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/new"));
    }

    @Test
    void createProduct() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());

        MockMultipartFile file = new MockMultipartFile("file", "file", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/my-products/new")
                        .file("file" ,file.getBytes()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-products"));
    }

    @Test
    void updateProductForm() throws Exception {
        when(productService.findById(anyLong())).thenReturn(product1);

        mockMvc.perform(get("/my-products/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/update"))
                .andExpect(model().attribute("product", product1));
    }

    @Test
    void updateProduct() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file", MediaType.MULTIPART_FORM_DATA_VALUE, new byte[0]);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/my-products/update").file("file", file.getBytes()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-products"));
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(get("/my-products/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/my-products"));
    }

    @Test
    void showProductImage() throws Exception {
        product1.setImage(new byte[0]);
        when(productService.findById(anyLong())).thenReturn(product1);

        mockMvc.perform(get("/products/1/image"))
                .andExpect(status().isOk());
    }

    @Test
    void addProductRating() throws Exception {
        when(userService.getCurrentUser()).thenReturn(new User());
        when(productService.findById(anyLong())).thenReturn(new Product());
        when(productRatingService.save(any())).thenReturn(new ProductRating());

        mockMvc.perform(post("/products/1/rating"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/1"));
    }

    @Test
    void deleteProductRating() throws Exception {
        mockMvc.perform(get("/products/1/rating/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/1"));
    }

    @Test
    void showFavoriteProducts() throws Exception {
        when(productService.findAll()).thenReturn(products);
        when(productFavoriteService.findAllObjectIdsByUser(any())).thenReturn(favorites);
        when(userService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/products/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/favorites"))
                .andExpect(model().attribute("products", emptyArray()));
    }

    @Test
    void addProductToFavorites() throws Exception {
        mockMvc.perform(get("/products/1/add-favorite"))
                .andExpect(status().isOk());
    }

    @Test
    void removeProductFromFavorites() throws Exception {
        mockMvc.perform(get("/products/1/remove-favorite"))
                .andExpect(status().isOk());
    }
}