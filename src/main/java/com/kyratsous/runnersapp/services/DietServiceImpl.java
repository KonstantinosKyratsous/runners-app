package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Diet;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.DietRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;

    public DietServiceImpl(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    @Override
    public Set<Diet> findAll() {
        Set<Diet> diets = new HashSet<>();
        dietRepository.findAll().forEach(diets::add);
        return diets;
    }

    @Override
    public Diet findById(Long id) {
        return dietRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Diet diet) {
        dietRepository.save(diet);
    }

    @Override
    public void delete(Diet diet) {
        dietRepository.delete(diet);
    }

    @Override
    public void deleteById(Long id) {
        if (!isOwnerOfDiet(findById(id).getNutritionist())) {
            System.out.println("You cannot delete this diet. You are not the owner of this diet post. ");
            return;
        }
        dietRepository.deleteById(id);
    }

    @Override
    public void update(Diet diet) {
        Diet currentDiet = findById(diet.getId());

        if (!isOwnerOfDiet(currentDiet.getNutritionist())) {
            System.out.println("You cannot update this diet. You are not the owner of this diet post. ");
            return;
        }

        currentDiet.setTitle(diet.getTitle());
        currentDiet.setBody(diet.getBody());

        save(currentDiet);
    }

    @Override
    public Set<Diet> findAllByUserId(User user) {
        Set<Diet> diets = new HashSet<>();

        if (user != null) {
            for (Diet diet: findAll()) {
                if (Objects.equals(diet.getNutritionist().getId(), user.getId())) {
                    diets.add(diet);
                }
            }
        }
        return diets;
    }

    private boolean isOwnerOfDiet(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return Objects.equals(auth.getName(), user.getUsername());
    }
}
