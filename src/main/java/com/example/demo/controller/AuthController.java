package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.event.LoginEvent;
import com.example.demo.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
public class AuthController {
// 在类的顶部定义 Logger 实例
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final String LOGIN_TOPIC = "user_login_events";

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getUsername());
        logger.info("Attempting to authenticate password: {}", loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        logger.info("Authentication successful for user: {}", authentication.getName());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = jwtUtil.createToken(loginRequest.getUsername());
        if(token != null && !token.isEmpty()) {
            LoginEvent loginEvent = new LoginEvent(loginRequest.getUsername(), LocalDateTime.now().toString());
            
            kafkaTemplate.send(LOGIN_TOPIC, loginRequest.getUsername(), loginEvent);
            return ResponseEntity.ok().body(Map.of("token", token)); 
        } else {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to generate token");
        }    
    }
}
