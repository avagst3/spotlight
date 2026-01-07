package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public interface BaseService {

    default Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof User user) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
