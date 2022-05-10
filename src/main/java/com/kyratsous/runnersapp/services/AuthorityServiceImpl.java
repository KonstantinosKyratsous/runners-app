package com.kyratsous.runnersapp.services;

import com.kyratsous.runnersapp.model.Authority;
import com.kyratsous.runnersapp.model.User;
import com.kyratsous.runnersapp.repositories.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Set<Authority> findAll() {
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findAll().forEach(authorities::add);
        return authorities;
    }

    @Override
    public Authority findById(Long id) {
        return authorityRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Authority authority) {
        authorityRepository.save(authority);
    }

    @Override
    public void delete(Authority authority) {
        authorityRepository.delete(authority);
    }

    @Override
    public void deleteById(Long id) {
        authorityRepository.deleteById(id);
    }

    @Override
    public void update(Authority authority) {
        Optional<Authority> currentAuthority = authorityRepository.findById(authority.getId());

        currentAuthority.get().setUser(authority.getUser());
        currentAuthority.get().setType(authority.getType());

        authorityRepository.save(currentAuthority.get());
    }

    @Override
    public Set<Authority> findAllByUserId(User user) {
        return null;
    }
}
