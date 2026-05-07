package com.sailesh.notes.auth.dto;

import com.sailesh.notes.user.Role;
import com.sailesh.notes.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private Role role;

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
  }
}
