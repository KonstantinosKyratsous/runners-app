package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService implements CrudService<Product, Long> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
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
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        if (isNotCurrentUser(findById(id).getCoach()))
            return;

        productRepository.deleteById(id);
    }

    @Override
    public void update(Product product) {
        Product currentProduct = findById(product.getId());

        if (isNotCurrentUser(currentProduct.getCoach()))
            return;

        currentProduct.setName(product.getName());
        currentProduct.setCategory(product.getCategory());
        currentProduct.setType(product.getType());
        currentProduct.setRate(product.getRate());
        if (product.getImage() != null)
            currentProduct.setImage(product.getImage());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setDescription(product.getDescription());
        currentProduct.setPros(product.getPros());
        currentProduct.setCons(product.getCons());

        productRepository.save(currentProduct);
    }

    @Override
    public Set<Product> findAllByUser(User user) {
        return (user != null)? productRepository.findAllByCoach(user): new HashSet<>();
    }

    public Set<Product> findFilteredProducts(Map<String, String> filters) {
        Set<Product> results;

        if (filters.get("order") != null) {
            results = Objects.equals(filters.get("order"), "asc")? productRepository.findAllByOrderByRateAsc(): productRepository.findAllByOrderByRateDesc();
        } else {
            results = findAll();
        }

        if (filters.get("category") != null) {
            Set<String> categories = new HashSet<>(Arrays.asList(filters.get("category").split(",")));
            results.retainAll(productRepository.findAllByCategoryIsIn(categories));
        }

        if (filters.get("type") != null) {
            Set<String> types = new HashSet<>(Arrays.asList(filters.get("type").split(",")));
            results.retainAll(productRepository.findAllByTypeIsIn(types));
        }

        if (filters.get("rate") != null) {
            String[] rates = filters.get("rate").split(",");
            double minRate = Double.parseDouble(rates[0]);
            double maxRate = Double.parseDouble(rates[1]);

            results.retainAll(productRepository.findAllByRateBetween(minRate, maxRate));
        }

        if (filters.get("price") != null) {
            String[] prices = filters.get("price").split(",");
            double minPrice = Double.parseDouble(prices[0]);
            double maxPrice = Double.parseDouble(prices[1]);

            results.retainAll(productRepository.findAllByPriceBetween(minPrice, maxPrice));
        }

        return results;
    }

    private boolean isNotCurrentUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
