package com.sailesh.notes.notes;

import com.sailesh.notes.common.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter; import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
  private final NoteService service;
  public NoteController(NoteService service){ this.service = service; }

  @GetMapping
  public ResponseEntity<?> list(){ return ResponseEntity.ok(service.findMyNotes()); }

  @PostMapping
  public ResponseEntity<?> add(@RequestBody NoteRequest req){
    var note = service.create(req.getTitle(), req.getDescription());
    return ResponseEntity.ok(ApiResponse.ok(note));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NoteRequest req){
    var note = service.update(id, req.getTitle(), req.getDescription());
    return ResponseEntity.ok(ApiResponse.ok(note));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    service.delete(id);
    return ResponseEntity.ok(ApiResponse.ok(Map.of("deleted", true)));
  }

  @Getter @Setter
  public static class NoteRequest {
    @NotBlank private String title;
    private String description;
  }
}
