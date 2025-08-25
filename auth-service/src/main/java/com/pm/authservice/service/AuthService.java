package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthService.class);
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(UserService userService, PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
    log.info("Authenticating user: {}", loginRequestDTO.getEmail());
    
    Optional<User> userOpt = userService.findByEmail(loginRequestDTO.getEmail());
    if (userOpt.isEmpty()) {
      log.warn("User not found: {}", loginRequestDTO.getEmail());
      return Optional.empty();
    }
    
    User user = userOpt.get();
    log.info("User found: {}, checking password", user.getEmail());
    
    boolean passwordMatches = passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword());
    log.info("Password matches: {}", passwordMatches);
    
    if (passwordMatches) {
      String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
      log.info("Token generated successfully");
      return Optional.of(token);
    }
    
    return Optional.empty();
  }

  public boolean validateToken(String token) {
    try {
      jwtUtil.validateToken(token);
      return true;
    } catch (JwtException e){
      return false;
    }
  }
}
