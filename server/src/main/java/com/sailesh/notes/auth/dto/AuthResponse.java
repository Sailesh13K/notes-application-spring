package com.sailesh.notes.auth.dto;
import com.sailesh.notes.user.User;
import lombok.*;

@Getter @Setter @AllArgsConstructor
public class AuthResponse {
  private String token;
  private User user;
}
