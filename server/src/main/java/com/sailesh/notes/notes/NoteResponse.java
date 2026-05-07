package com.sailesh.notes.notes;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoteResponse {
  private Long id;
  private String title;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;

  public static NoteResponse from(Note note) {
    return new NoteResponse(
      note.getId(),
      note.getTitle(),
      note.getDescription(),
      note.getCreatedAt(),
      note.getUpdatedAt()
    );
  }
}
