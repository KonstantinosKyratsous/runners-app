package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.ProductRating;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.ProductRatingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ProductRatingServiceImpl implements ProductRatingService {
    private final ProductRatingRepository productRatingRepository;

    public ProductRatingServiceImpl(ProductRatingRepository productRatingRepository) {
        this.productRatingRepository = productRatingRepository;
    }

    @Override
    public Set<ProductRating> findAll() {
        Set<ProductRating> productRatings = new HashSet<>();
        productRatingRepository.findAll().forEach(productRatings::add);
        return productRatings;
    }

    @Override
    public ProductRating findById(Long id) {
        return productRatingRepository.findById(id).orElse(null);
    }

    @Override
    public void save(ProductRating productRating) {
        productRatingRepository.save(productRating);
    }

    @Override
    public void delete(ProductRating productRating) {
        if (!isNotOwnerOfRating(productRating.getUser()))
            productRatingRepository.delete(productRating);
    }

    @Override
    public void deleteById(Long id) {
        if (!isNotOwnerOfRating(findById(id).getUser()))
            productRatingRepository.deleteById(id);
    }

    @Override
    public void update(ProductRating productRating) {
        ProductRating currentProductRating = findById(productRating.getId());

        if (isNotOwnerOfRating(currentProductRating.getUser())) {
            System.out.println("You cannot update this product rating. You are not the owner. ");
            return;
        }

        currentProductRating.setDescription(productRating.getDescription());
        currentProductRating.setRate(productRating.getRate());
        currentProductRating.setUpdated(true);

        productRatingRepository.save(currentProductRating);
    }

    @Override
    public Set<ProductRating> findAllByUserId(User user) {
        return null;
    }

    @Override
    public Set<ProductRating> findAllByProductId(Long id) {
        Set<ProductRating> ratings = new HashSet<>();

        for (ProductRating rating: findAll()) {
            if (Objects.equals(rating.getProduct().getId(), id)) {
                ratings.add(rating);
            }
        }

        return ratings;
    }
    private boolean isNotOwnerOfRating(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return !Objects.equals(auth.getName(), user.getUsername());
    }

}
