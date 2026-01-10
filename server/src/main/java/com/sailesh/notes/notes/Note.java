package com.sailesh.notes.notes;

import com.sailesh.notes.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="notes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Note {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false) private String title;
  @Column(columnDefinition="TEXT") private String description;

  private Instant createdAt;
  private Instant updatedAt;

  @ManyToOne(optional=false, fetch=FetchType.LAZY)
  @JoinColumn(name="user_id")
  private User user;

  @PrePersist void onCreate(){ createdAt=Instant.now(); updatedAt=createdAt; }
  @PreUpdate  void onUpdate(){ updatedAt=Instant.now(); }
}
