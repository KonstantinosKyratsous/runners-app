package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Set<Product> findAll() {
        Set<Product> products = new HashSet<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteById(Long id) {
        if (!isOwnerOfProduct(findById(id).getCoach())) {
            System.out.println("You cannot delete this product. You are not the owner of this product. ");
            return;
        }
        productRepository.deleteById(id);
    }

    @Override
    public void update(Product product) {
        Product currentProduct = findById(product.getId());

        if (!isOwnerOfProduct(currentProduct.getCoach())) {
            System.out.println("You cannot delete this product. You are not the owner of this product. ");
            return;
        }

        currentProduct.setName(product.getName());
        currentProduct.setType(product.getType());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setDescription(product.getDescription());
        currentProduct.setImage(product.getImage());

        productRepository.save(currentProduct);
    }

    @Override
    public Set<Product> findAllByUserId(User user) {
        Set<Product> products = new HashSet<>();

        if (user != null) {
            for (Product product: findAll()) {
                if (Objects.equals(product.getCoach().getId(), user.getId())) {
                    products.add(product);
                }
            }
        }

        return products;
    }

    @Override
    public Set<String> findProductTypes() {
        Set<Product> products = findAll();
        Set<String> productTypes = new HashSet<>();

        if (!products.isEmpty()) {
            for (Product product: products) {
                if (!productTypes.contains(product.getType())) {
                    productTypes.add(product.getType());
                }
            }
        }

        return productTypes;
    }

    private boolean isOwnerOfProduct(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return Objects.equals(auth.getName(), user.getUsername());
    }
}
