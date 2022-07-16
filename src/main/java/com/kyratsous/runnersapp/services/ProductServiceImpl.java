package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Product;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if (isNotOwnerOfProduct(findById(id).getCoach())) {
            System.out.println("You cannot delete this product. You are not the owner of this product. ");
            return;
        }
        productRepository.deleteById(id);
    }

    @Override
    public void update(Product product) {
        Product currentProduct = findById(product.getId());

        if (isNotOwnerOfProduct(currentProduct.getCoach())) {
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
            for (Product product : findAll()) {
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
            for (Product product : products) {
                if (!productTypes.contains(product.getType())) {
                    productTypes.add(product.getType());
                }
            }
        }

        return productTypes;
    }

    @Override
    public Set<Product> findFilteredProducts(Map<String, String> filters) {
        Set<Product> results = new HashSet<>();
        Set<Product> resultsByType = new HashSet<>();
        Set<Product> resultsByPrice = new HashSet<>();

        if (filters.get("type") != null) {
            String[] types = filters.get("type").split(",");

            for (Product product : findAll())
                if (Arrays.asList(types).contains(product.getType()))
                    resultsByType.add(product);
        }

        if (filters.get("price") != null) {
            String[] prices = filters.get("price").split(",");
            double minPrice = Double.parseDouble(prices[0]);
            double maxPrice = Double.parseDouble(prices[1]);

            for (Product product : findAll())
                if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                    resultsByPrice.add(product);
        }

        if (resultsByType.isEmpty())
            return resultsByPrice;
        else if (resultsByPrice.isEmpty())
            return resultsByType;
        else
            for (Product product : resultsByType)
                if (resultsByPrice.contains(product))
                    results.add(product);

        return results;
    }

    private boolean isNotOwnerOfProduct(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
