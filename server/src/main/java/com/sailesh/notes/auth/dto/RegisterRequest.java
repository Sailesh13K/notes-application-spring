package com.sailesh.notes.auth.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class RegisterRequest {
  @NotBlank private String name;
  @Email @NotBlank private String email;
  @Size(min=5) @NotBlank private String password;
}
