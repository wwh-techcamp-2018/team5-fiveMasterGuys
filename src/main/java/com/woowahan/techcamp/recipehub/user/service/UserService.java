package com.woowahan.techcamp.recipehub.user.service;

import com.woowahan.techcamp.recipehub.common.exception.ResourceExistsException;
import com.woowahan.techcamp.recipehub.common.exception.UnauthorizedException;
import com.woowahan.techcamp.recipehub.common.support.Message;
import com.woowahan.techcamp.recipehub.user.domain.User;
import com.woowahan.techcamp.recipehub.user.dto.LoginDTO;
import com.woowahan.techcamp.recipehub.user.dto.SignupDTO;
import com.woowahan.techcamp.recipehub.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;


    public User create(SignupDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceExistsException(messageSourceAccessor.getMessage(Message.EMAIL_EXISTS));
        }
        return userRepository.save(dto.toEntity(passwordEncoder));
    }

    public User login(LoginDTO dto) {
        return userRepository.findByEmail(dto.getEmail())
                .filter(user -> user.matchPassword(dto.getPassword(), passwordEncoder))
                .orElseThrow(() -> new UnauthorizedException(messageSourceAccessor.getMessage(Message.LOGIN_FAILED)));
    }
}
