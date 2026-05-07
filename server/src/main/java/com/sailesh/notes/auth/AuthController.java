package com.sailesh.notes.auth;

import com.sailesh.notes.auth.dto.LoginRequest;
import com.sailesh.notes.auth.dto.RegisterRequest;
import com.sailesh.notes.auth.dto.UserResponse;
import com.sailesh.notes.common.ApiResponse;
import com.sailesh.notes.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService service;

  public AuthController(AuthService service){
    this.service = service;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
    service.register(req);
    return ResponseEntity.ok(ApiResponse.ok("Registered"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req){
    return ResponseEntity.ok(service.login(req));
  }

  @GetMapping("/verify")
  public ResponseEntity<?> verify(@AuthenticationPrincipal UserDetails principal){
    if (principal == null) {
      return ResponseEntity.ok(ApiResponse.fail("Invalid token"));
    }

    User user = service.verify(principal.getUsername());
    if (user == null) {
      return ResponseEntity.ok(ApiResponse.fail("Invalid token"));
    }

    return ResponseEntity.ok(ApiResponse.ok(UserResponse.from(user)));
  }
}
