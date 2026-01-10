package com.sailesh.notes.auth;

import com.sailesh.notes.auth.dto.*;
import com.sailesh.notes.user.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;   // ← ADD THIS

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwtService;

  public AuthService(UserRepository users, PasswordEncoder encoder,
                     AuthenticationManager am, JwtService jwtService){
    this.users = users;
    this.encoder = encoder;
    this.authManager = am;
    this.jwtService = jwtService;
  }

  public User register(RegisterRequest req){
    if (users.existsByEmail(req.getEmail()))
      throw new RuntimeException("Email already used");

    User u = User.builder()
      .name(req.getName())
      .email(req.getEmail())
      .password(encoder.encode(req.getPassword()))
      .role(Role.USER)
      .build();

    return users.save(u);
  }

  public AuthResponse login(LoginRequest req){
    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
    );

    User u = users.findByEmail(req.getEmail()).orElseThrow();

    String token = jwtService.generate(
      u.getEmail(),
      Map.of("role", u.getRole().name(), "name", u.getName())
    );

    return new AuthResponse(token, u);
  }

  public User verify(String email){
    return users.findByEmail(email).orElse(null);
  }
}
