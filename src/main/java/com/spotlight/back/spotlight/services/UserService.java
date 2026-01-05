package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.dtos.UserLoginDto;
import com.spotlight.back.spotlight.models.dtos.UserRegisterDto;
import com.spotlight.back.spotlight.models.entities.User;
import com.spotlight.back.spotlight.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserRegisterDto dto) {
        String hashed = BCrypt.hashpw(dto.password, BCrypt.gensalt());

        User user = new User(
                dto.name,
                dto.email,
                hashed,
                dto.profilePictureUrl
        );

        return userRepository.save(user);
    }

    public User login(UserLoginDto dto) {
        return userRepository.findByEmail(dto.email)
                .filter(u -> BCrypt.checkpw(dto.password, u.getPasswordHash()))
                .orElse(null);
    }
}
