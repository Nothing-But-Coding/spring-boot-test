package com.example.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.config.SecurityConfig;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@Import(SecurityConfig.class) // 如果UserController也需要安全配置
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService; // 模拟UserDetailsService

    // 保留所有相关的@MockBean定义
    @MockBean
    private UserService userService;

    // 如果UserController需要JwtUtil或其他服务，也要模拟它们
    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    
    @Test
    void createUser_ReturnsUserOnSuccess() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("email");
        when(userService.createUser(any(User.class))).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()));

        verify(userService, times(1)).createUser(any(User.class));
    }


}
