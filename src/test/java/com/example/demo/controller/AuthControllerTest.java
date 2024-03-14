package com.example.demo.controller;

import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.LoginRequest;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // 导入安全配置
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService; // 模拟UserDetailsService

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticateUser_ReturnsTokenOnSuccess() throws Exception {
        // Arrange
        String username = "testUser";
        String password = "password";
        String expectedToken = "dummyToken";
        LoginRequest loginRequest = new LoginRequest(username, password);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(jwtUtil.createToken(username)).thenReturn(expectedToken);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedToken)));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).createToken(username);
        verify(kafkaTemplate, times(1)).send(anyString(), eq(username), any());
    }
}
