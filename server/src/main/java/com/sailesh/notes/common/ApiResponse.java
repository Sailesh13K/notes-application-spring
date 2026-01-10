package com.sailesh.notes.common;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ApiResponse<T> {
  private boolean success;
  private T data;
  private String message;

  public static <T> ApiResponse<T> ok(T data){ return new ApiResponse<>(true, data, null); }
  public static <T> ApiResponse<T> fail(String msg){ return new ApiResponse<>(false, null, msg); }
}
