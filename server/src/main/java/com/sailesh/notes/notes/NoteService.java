package com.sailesh.notes.notes;

import com.sailesh.notes.user.User;
import com.sailesh.notes.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {
  private final NoteRepository notes;
  private final UserRepository users;

  public NoteService(NoteRepository notes, UserRepository users){
    this.notes = notes; this.users = users;
  }

  private User me(){
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return users.findByEmail(email).orElseThrow();
  }

  public List<Note> findMyNotes(){ return notes.findByUser(me()); }

  public Note create(String title, String description){
    Note n = Note.builder().title(title).description(description).user(me()).build();
    return notes.save(n);
  }

  public Note update(Long id, String title, String description){
    Note n = notes.findById(id).orElseThrow();
    if(!n.getUser().getId().equals(me().getId())) throw new RuntimeException("Forbidden");
    n.setTitle(title); n.setDescription(description);
    return notes.save(n);
  }

  public void delete(Long id){
    Note n = notes.findById(id).orElseThrow();
    if(!n.getUser().getId().equals(me().getId())) throw new RuntimeException("Forbidden");
    notes.delete(n);
  }
}
