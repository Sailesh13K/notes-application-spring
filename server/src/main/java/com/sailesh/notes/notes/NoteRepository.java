package com.sailesh.notes.notes;

import com.sailesh.notes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
  List<Note> findByUser(User user);
}
