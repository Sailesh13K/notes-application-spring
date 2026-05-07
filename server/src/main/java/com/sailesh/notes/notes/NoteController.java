package com.sailesh.notes.notes;

import com.sailesh.notes.common.ApiResponse;
import com.sailesh.notes.common.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
  private final NoteService service;

  public NoteController(NoteService service){
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<?> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String query){
    var pageable = PageRequest.of(
      Math.max(page, 0),
      Math.min(Math.max(size, 1), 50),
      Sort.by(Sort.Direction.DESC, "updatedAt")
    );
    var noteResponses = service.findMyNotes(query, pageable).map(NoteResponse::from);
    return ResponseEntity.ok(ApiResponse.ok(PageResponse.from(noteResponses)));
  }

  @GetMapping("/search")
  public ResponseEntity<?> search(
      @RequestParam String query,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size){
    return list(page, size, query);
  }

  @PostMapping
  public ResponseEntity<?> add(@Valid @RequestBody NoteRequest req){
    var note = service.create(req.getTitle(), req.getDescription());
    return ResponseEntity.ok(ApiResponse.ok(NoteResponse.from(note)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody NoteRequest req){
    var note = service.update(id, req.getTitle(), req.getDescription());
    return ResponseEntity.ok(ApiResponse.ok(NoteResponse.from(note)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id){
    service.delete(id);
    return ResponseEntity.ok(ApiResponse.ok(Map.of("deleted", true)));
  }

  @Getter
  @Setter
  public static class NoteRequest {
    @NotBlank private String title;
    private String description;
  }
}
