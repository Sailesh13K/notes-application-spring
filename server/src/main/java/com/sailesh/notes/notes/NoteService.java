package com.sailesh.notes.notes;

import com.sailesh.notes.user.User;
import com.sailesh.notes.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NoteService {
  private final NoteRepository notes;
  private final UserRepository users;

  public NoteService(NoteRepository notes, UserRepository users){
    this.notes = notes;
    this.users = users;
  }

  private User me(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return users.findByEmail(email)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user"));
  }

  public Page<Note> findMyNotes(String query, Pageable pageable){
    if (query == null || query.isBlank()) {
      return notes.findByUser(me(), pageable);
    }
    return notes.searchByUser(me(), query.trim(), pageable);
  }

  public Note create(String title, String description){
    Note n = Note.builder().title(title).description(description).user(me()).build();
    return notes.save(n);
  }

  public Note update(Long id, String title, String description){
    Note n = notes.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    if (!n.getUser().getId().equals(me().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot modify this note");
    }
    n.setTitle(title);
    n.setDescription(description);
    return notes.save(n);
  }

  public void delete(Long id){
    Note n = notes.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    if (!n.getUser().getId().equals(me().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this note");
    }
    notes.delete(n);
  }
}
