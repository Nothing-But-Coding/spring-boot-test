package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 从Spring 4.3版本开始，如果一个类只定义了一个构造器，Spring会自动将这个构造器用于依赖注入，即使没有使用@Autowired注解。
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 保存用户
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
