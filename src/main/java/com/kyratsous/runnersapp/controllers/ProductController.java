package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.ProductRating;
import com.kyratsous.runnersapp.services.ProductRatingService;
import com.kyratsous.runnersapp.services.ProductService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductRatingService productRatingService;

    public ProductController(ProductService productService, UserService userService, ProductRatingService productRatingService) {
        this.productService = productService;
        this.userService = userService;
        this.productRatingService = productRatingService;
    }

    @RequestMapping("/products")
    public String getProducts(Model model, @RequestParam(required=false) Map<String,String> filters) {
        model.addAttribute("products", filters.isEmpty()? productService.findAll(): productService.findFilteredProducts(filters));
        model.addAttribute("types", productService.findProductTypes());

        return "products/index";
    }

    @RequestMapping("/products/{id}")
    public String showProduct(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("ratings", productRatingService.findAllByProductId(id));
        model.addAttribute("newRating", new ProductRating());

        return "products/show";
    }

    @RequestMapping("/my-products")
    public String getMyProducts(Model model) {
        model.addAttribute("products", productService.findAllByUserId(userService.getCurrentUser()));

        return "products/my-products";
    }

    @RequestMapping("/my-products/new")
    public String createProductForm() {
        return "products/new";
    }

    @PostMapping("/my-products/new")
    public String createProduct(@RequestParam("name") String name,
                                @RequestParam("price") double price,
                                @RequestParam("type") String type,
                                @RequestParam("description") String description,
                                final @RequestParam("image") MultipartFile file) throws IOException {

        byte[] imageData = file.getBytes();

        Product product = new Product();
        product.setCoach(userService.getCurrentUser());
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setType(type);
        product.setImage(imageData);
        productService.save(product);

        return "redirect:/my-products";
    }

    @RequestMapping("/my-products/{id}/update")
    public String updateProductForm(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));

        return "products/update";
    }

    @PostMapping("/my-products/{id}/update")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam("name") String name,
                                @RequestParam("price") double price,
                                @RequestParam("type") String type,
                                @RequestParam("description") String description,
                                final @RequestParam("image") MultipartFile file) throws IOException {

        Product product = productService.findById(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setType(type);
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

    @PostMapping("/products/{id}/rating")
    public String addProductRating(@PathVariable Long id, @ModelAttribute("newRating") ProductRating rating) {
        rating.setUser(userService.getCurrentUser());
        rating.setProduct(productService.findById(id));
        productRatingService.save(rating);

        return "redirect:/products/" + id;
    }

    @GetMapping("/products/{id}/rating/{rate_id}/delete")
    public String deleteProductRating(@PathVariable Long id, @PathVariable Long rate_id) {

        // Move to service
        if (userService.getCurrentUser() != null && productRatingService.findById(rate_id).getUser().getUsername().contentEquals(userService.getCurrentUser().getUsername()))
            productRatingService.deleteById(rate_id);

        return "redirect:/products/" + id;
    }
}
