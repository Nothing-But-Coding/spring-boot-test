package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/users")
public class UserController {

    private List<User> users = new ArrayList<>();

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        users.add(user);
        return ResponseEntity.ok(user);
    }

}
