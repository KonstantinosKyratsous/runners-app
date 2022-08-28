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
public class DietService implements CrudService<Diet, Long> {

    private final DietRepository dietRepository;

    public DietService(DietRepository dietRepository) {
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
    public Diet save(Diet diet) {
        return dietRepository.save(diet);
    }

    @Override
    public void deleteById(Long id) {
        if (isNotCurrentUser(findById(id).getNutritionist()))
            return;

        dietRepository.deleteById(id);
    }

    @Override
    public void update(Diet diet) {
        Diet currentDiet = findById(diet.getId());

        if (isNotCurrentUser(currentDiet.getNutritionist()))
            return;

        currentDiet.setTitle(diet.getTitle());
        currentDiet.setBody(diet.getBody());

        save(currentDiet);
    }

    @Override
    public Set<Diet> findAllByUser(User user) {
        return (user != null)? dietRepository.findAllByNutritionist(user): new HashSet<>();
    }

    private boolean isNotCurrentUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !Objects.equals(auth.getName(), user.getUsername());
    }
}
