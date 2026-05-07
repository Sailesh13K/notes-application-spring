package com.sailesh.notes.auth;

import com.sailesh.notes.auth.dto.AuthResponse;
import com.sailesh.notes.auth.dto.LoginRequest;
import com.sailesh.notes.auth.dto.RegisterRequest;
import com.sailesh.notes.auth.dto.UserResponse;
import com.sailesh.notes.user.Role;
import com.sailesh.notes.user.User;
import com.sailesh.notes.user.UserRepository;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    if (users.existsByEmail(req.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already used");
    }

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

    User u = users.findByEmail(req.getEmail())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

    String token = jwtService.generate(
      u.getEmail(),
      Map.of("role", u.getRole().name(), "name", u.getName())
    );

    return new AuthResponse(token, UserResponse.from(u));
  }

  public User verify(String email){
    return users.findByEmail(email).orElse(null);
  }
}
