package com.woowahan.techcamp.recipehub.user.service;

import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.exception.ConflictException;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.domain.UserRepository;
import com.woowahan.techcamp.recipehub.user.dto.LoginDto;
import com.woowahan.techcamp.recipehub.user.dto.SignupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(LoginDto dto) {
        return userRepository.findByEmail(dto.getEmail())
                .filter(user -> user.matchPassword(dto.getPassword(), passwordEncoder))
                .orElseThrow(UnauthorizedException::new);
    }

    public User add(SignupDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException();
        }
        return userRepository.save(dto.toEntity(passwordEncoder));
    }
}
