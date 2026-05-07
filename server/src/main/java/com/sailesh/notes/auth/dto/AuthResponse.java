package com.sailesh.notes.auth.dto;
import lombok.*;

@Getter @Setter @AllArgsConstructor
public class AuthResponse {
  private String token;
  private UserResponse user;
}
