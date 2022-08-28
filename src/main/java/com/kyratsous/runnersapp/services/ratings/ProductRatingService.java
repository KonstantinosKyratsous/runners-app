package com.kyratsous.runnersapp.services.ratings;

import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.model.ratings.ProductRating;
import com.kyratsous.runnersapp.repositories.ratings.ProductRatingRepository;
import com.kyratsous.runnersapp.services.CrudService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class ProductRatingService implements CrudService<ProductRating, Long> {
    private final ProductRatingRepository productRatingRepository;

    public ProductRatingService(ProductRatingRepository productRatingRepository) {
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
    public ProductRating save(ProductRating productRating) {
        return productRatingRepository.save(productRating);
    }

    @Override
    public void deleteById(Long id) {
        if (isCurrentUserOwnerOfRating(findById(id).getUser()))
            productRatingRepository.deleteById(id);
    }

    @Override
    public void update(ProductRating productRating) {
        ProductRating currentProductRating = findById(productRating.getId());

        if (!isCurrentUserOwnerOfRating(currentProductRating.getUser()))
            return;

        currentProductRating.setDescription(productRating.getDescription());
        currentProductRating.setRate(productRating.getRate());

        productRatingRepository.save(currentProductRating);
    }

    @Override
    public Set<ProductRating> findAllByUser(User user) {
        return productRatingRepository.findAllByUser(user);
    }

    public Set<ProductRating> findAllByProductId(Long id) {
        return productRatingRepository.findAllByProductId(id);
    }
    private boolean isCurrentUserOwnerOfRating(User ownerUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(auth.getName(), ownerUser.getUsername());
    }

}
