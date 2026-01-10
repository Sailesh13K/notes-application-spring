package com.sailesh.notes.auth;

import com.sailesh.notes.auth.dto.*;
import com.sailesh.notes.common.ApiResponse;
import com.sailesh.notes.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService service;
  public AuthController(AuthService service){ this.service = service; }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest req){
    User u = service.register(req);
    return ResponseEntity.ok(ApiResponse.ok("Registered"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody LoginRequest req){
    var resp = service.login(req);
    return ResponseEntity.ok(resp); // { token, user }
  }

  @GetMapping("/verify")
  public ResponseEntity<?> verify(@AuthenticationPrincipal UserDetails principal){
    if (principal == null) return ResponseEntity.ok(ApiResponse.fail("Invalid token"));
    return ResponseEntity.ok(ApiResponse.builder().success(true).data(
      java.util.Map.of("email", principal.getUsername())
    ).build());
  }
}
