package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.favorites.ProductFavorite;
import com.kyratsous.runnersapp.model.ratings.ProductRating;
import com.kyratsous.runnersapp.services.ProductService;
import com.kyratsous.runnersapp.services.UserService;
import com.kyratsous.runnersapp.services.favorites.ProductFavoriteService;
import com.kyratsous.runnersapp.services.ratings.ProductRatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductRatingService productRatingService;
    private final ProductFavoriteService productFavoriteService;

    public ProductController(ProductService productService, UserService userService, ProductRatingService productRatingService, ProductFavoriteService productFavoriteService) {
        this.productService = productService;
        this.userService = userService;
        this.productRatingService = productRatingService;
        this.productFavoriteService = productFavoriteService;
    }

    @RequestMapping("/products")
    public String getProducts(Model model, @RequestParam(required=false) Map<String,String> filters) {
        User currentUser = userService.getCurrentUser();
        //if (currentUser != null && filters.isEmpty()) {
        //    return "redirect:/products?" + currentUser.getPreferencesToString();
        //}

        model.addAttribute("products", filters.isEmpty()? productService.findAll(): productService.findFilteredProducts(filters));
        model.addAttribute("favorites", productFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()));

        return "products/index";
    }

    @RequestMapping("/products/{id}")
    public String showProduct(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("ratings", productRatingService.findAllByProductId(id));
        model.addAttribute("newRating", new ProductRating());
        model.addAttribute("isFavorite", productFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(id));

        return "products/show";
    }

    @RequestMapping("/my-products")
    public String getMyProducts(Model model) {
        model.addAttribute("products", productService.findAllByUser(userService.getCurrentUser()));

        return "products/my-products";
    }

    @RequestMapping("/my-products/new")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/new";
    }

    @PostMapping("/my-products/new")
    public String createProduct(@ModelAttribute("product") Product product,
                                final @RequestParam("file") MultipartFile file) throws IOException {

        product.setCoach(userService.getCurrentUser());
        product.setImage(file.getBytes());
        product.setDate(new Date());

        productService.save(product);

        return "redirect:/my-products";
    }

    @RequestMapping("/my-products/{id}/update")
    public String updateProductForm(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));

        return "products/update";
    }

    @PostMapping("/my-products/update")
    public String updateProduct(@ModelAttribute("product") Product product,
                                final @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty())
            product.setImage(file.getBytes());

        productService.update(product);

        return "redirect:/my-products";
    }

    @GetMapping("/my-products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);

        return "redirect:/my-products";
    }

    @GetMapping("/products/{id}/image")
    public void showProductImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");

        Product product = productService.findById(id);

        byte[] imageData = product.getImage();

        OutputStream o = response.getOutputStream();
        o.write(imageData);
        o.flush();
        o.close();
    }

    @PostMapping("/products/{product_id}/rating")
    public String addProductRating(@PathVariable Long product_id, @ModelAttribute("newRating") ProductRating rating) {
        rating.setUser(userService.getCurrentUser());
        rating.setProduct(productService.findById(product_id));
        productRatingService.save(rating);

        return "redirect:/products/" + product_id;
    }

    @GetMapping("/products/{id}/rating/{rate_id}/delete")
    public String deleteProductRating(@PathVariable Long id, @PathVariable Long rate_id) {
        productRatingService.deleteById(rate_id);

        return "redirect:/products/" + id;
    }

    @RequestMapping("/products/favorites")
    public String showFavoriteProducts(Model model) {
        model.addAttribute("products",
                productService.findAll().stream().filter(product ->
                        productFavoriteService.findAllObjectIdsByUser(userService.getCurrentUser()).contains(product.getId())).toArray());

        return "products/favorites";
    }

    @GetMapping("/products/{id}/add-favorite")
    @ResponseBody
    public String addProductToFavorites(@PathVariable Long id) {
        productFavoriteService.save(new ProductFavorite(productService.findById(id), userService.getCurrentUser()));
        return "ok";
    }

    @GetMapping("/products/{id}/remove-favorite")
    @ResponseBody
    public String removeProductFromFavorites(@PathVariable Long id) {
        productFavoriteService.deleteByObjectId(id, userService.getCurrentUser());
        return "ok";
    }
}
