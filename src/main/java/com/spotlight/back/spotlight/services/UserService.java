package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.models.converters.UserConverter;
import com.spotlight.back.spotlight.models.dtos.UserLoginDto;
import com.spotlight.back.spotlight.models.dtos.UserRegisterDto;
import com.spotlight.back.spotlight.models.dtos.UserResponceDto;
import com.spotlight.back.spotlight.models.entities.User;
import com.spotlight.back.spotlight.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final UserConverter userConverter;
    private final com.spotlight.back.spotlight.managers.JwtManager jwtManager;

    @Transactional
    public UserResponceDto register(UserRegisterDto dto) {
        String hashed = BCrypt.hashpw(dto.password, BCrypt.gensalt());

        User user = User.builder()
                .name(dto.username)
                .username(dto.username)
                .password(hashed)
                .profilePictureUrl("")
                .build();
        
        user = userRepository.save(user); // Save first to get ID
        UserResponceDto response = userConverter.convert(user);
        response.setToken(jwtManager.generateToken(user));
        return response;
    }

    @Transactional
    public UserResponceDto updateUserProfilePicture(UUID id,MultipartFile file) {
        User user = getUserByIdUtils(id);

        String filename = fileUploadService.uploadFile(file, "user");
        user.setProfilePictureUrl(filename);
        return userConverter.convert(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponceDto login(UserLoginDto dto) {
        User user = userRepository.findByUsername(dto.username)
                .filter(u -> BCrypt.checkpw(dto.password, u.getPassword()))
                .orElse(null);

        if (user == null) {
            return null;
        }

        UserResponceDto response = userConverter.convert(user);
        response.setToken(jwtManager.generateToken(user));
        return response;
    }

    @Transactional(readOnly = true)
    public java.util.List<UserResponceDto> getAllUsers() {
        return userRepository.findAll().stream().map(userConverter::convert).toList();
    }

    @Transactional(readOnly = true)
    public User getUserByIdUtils(java.util.UUID id) {
        return userRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public UserResponceDto getUserById(java.util.UUID id) {
        return userConverter.convert(userRepository.findById(id).get());
    }

    @Transactional
    public boolean deleteUser(java.util.UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }    
}
