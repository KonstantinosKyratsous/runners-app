package com.kyratsous.runnersapp.controllers;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.services.ProductService;
import com.kyratsous.runnersapp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;


@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;


    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @RequestMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("types", productService.findProductTypes());

        return "products/index";
    }

    @RequestMapping("/my-products")
    public String getMyProducts(Model model) {
        model.addAttribute("products", productService.findAllByUserId(userService.getCurrentUser()));

        return "products/my-products";
    }

    @RequestMapping("/my-products/new")
    public String createProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);

        MultipartFile file = null;
        model.addAttribute("image", file);
        return "products/new";
    }

    @PostMapping("/my-products/new")
    public String createProduct(@ModelAttribute("product") Product product) throws SQLException, IOException {
        product.setCoach(userService.getCurrentUser());
        productService.save(product);

        //Getting the connection
//        String mysqlUrl = "jdbc:mysql://localhost/runners";
//        Connection con = DriverManager.getConnection(mysqlUrl, "root", "root");
//        System.out.println("Connection established......");
//        PreparedStatement pstmt = con.prepareStatement("INSERT INTO products VALUES(?,?,?,?,?,?,?)");
//        pstmt.setLong(1, product.getId());
//        pstmt.setString(2, product.getDescription());
//        //Inserting Blob type
//        InputStream in = new FileInputStream("C:\\Users\\Konstantinos\\Downloads\\new_balance.jpg");
//        pstmt.setBlob(3, in);
//        pstmt.setString(4, product.getName());
//        pstmt.setDouble(5, product.getPrice());
//        pstmt.setString(6, product.getType());
//        pstmt.setLong(7, product.getCoach().getId());
//        //Executing the statement
//        pstmt.execute();
//        System.out.println("Record inserted......");

        return "redirect:/my-products";
    }

    @RequestMapping("/my-products/{id}/update")
    public String updateProductForm(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));

        return "products/update";
    }

    @PostMapping("/my-products/{id}/update")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("product") Product product) {
        productService.update(product);

        return "redirect:/my-products";
    }

    @GetMapping("/my-products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);

        return "redirect:/my-products";
    }

    @GetMapping("/products/{id}/image")
    public void showProductImage(@PathVariable Long id, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("image/png");

        Product product = productService.findById(id);

        //int blobLength = (int) product.getImage().length();
        //byte [] imageData = product.getImage().getBytes(1, blobLength);

        byte [] imageData = product.getImage();

        OutputStream o = response.getOutputStream();
        o.write(imageData);
        o.flush();
        o.close();

//        InputStream is = new ByteArrayInputStream(product.getImage());
//        IOUtils.copyLarge(is, response.getOutputStream());
    }

}
