package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    
    /**
     * 从Spring 4.3版本开始，如果一个类只定义了一个构造器，Spring会自动将这个构造器用于依赖注入，即使没有使用@Autowired注解。
    */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Accessing get all users");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            logger.info("Attempting to create user");
            User savedUser = userService.createUser(user);
            logger.info("User created successfully");
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    } catch (Exception e) {
            logger.error("Error creating user", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
