package com.sailesh.notes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class NotesApiIntegrationTests {

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void registerLoginAndVerifyReturnsSafeUser() throws Exception {
    register("Sailesh", "sailesh@example.com", "secret123");

    String token = login("sailesh@example.com", "secret123");

    mvc.perform(get("/api/auth/verify")
        .header("Authorization", "Bearer " + token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data.email").value("sailesh@example.com"))
      .andExpect(jsonPath("$.data.password").doesNotExist());
  }

  @Test
  void authenticatedUserCanCreateAndListOwnNotes() throws Exception {
    register("Sailesh", "sailesh@example.com", "secret123");
    String token = login("sailesh@example.com", "secret123");

    mvc.perform(post("/api/notes")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json(Map.of("title", "First note", "description", "Useful details"))))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data.title").value("First note"))
      .andExpect(jsonPath("$.data.user").doesNotExist());

    mvc.perform(get("/api/notes")
        .header("Authorization", "Bearer " + token))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data.content.length()").value(1))
      .andExpect(jsonPath("$.data.content[0].title").value("First note"))
      .andExpect(jsonPath("$.data.totalElements").value(1));
  }

  @Test
  void notesCanBePagedAndSearched() throws Exception {
    register("Sailesh", "sailesh@example.com", "secret123");
    String token = login("sailesh@example.com", "secret123");
    createNote(token, "Spring Security", "JWT notes");
    createNote(token, "React Router", "Frontend notes");
    createNote(token, "PostgreSQL", "Database notes");

    mvc.perform(get("/api/notes")
        .header("Authorization", "Bearer " + token)
        .param("page", "0")
        .param("size", "2"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.content.length()").value(2))
      .andExpect(jsonPath("$.data.totalElements").value(3))
      .andExpect(jsonPath("$.data.totalPages").value(2));

    mvc.perform(get("/api/notes/search")
        .header("Authorization", "Bearer " + token)
        .param("query", "react")
        .param("page", "0")
        .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.content.length()").value(1))
      .andExpect(jsonPath("$.data.content[0].title").value("React Router"));
  }

  @Test
  void userCannotUpdateAnotherUsersNote() throws Exception {
    register("Owner", "owner@example.com", "secret123");
    register("Intruder", "intruder@example.com", "secret123");
    String ownerToken = login("owner@example.com", "secret123");
    String intruderToken = login("intruder@example.com", "secret123");

    long noteId = createNote(ownerToken, "Private note", "Owner only");

    mvc.perform(put("/api/notes/" + noteId)
        .header("Authorization", "Bearer " + intruderToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json(Map.of("title", "Changed", "description", "Nope"))))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.success").value(false));
  }

  @Test
  void unauthenticatedUserCannotAccessNotes() throws Exception {
    mvc.perform(get("/api/notes"))
      .andExpect(status().isForbidden());
  }

  private void register(String name, String email, String password) throws Exception {
    mvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json(Map.of("name", name, "email", email, "password", password))))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true));
  }

  private String login(String email, String password) throws Exception {
    String body = mvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json(Map.of("email", email, "password", password))))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").isString())
      .andExpect(jsonPath("$.user.email").value(email))
      .andExpect(jsonPath("$.user.password").doesNotExist())
      .andReturn()
      .getResponse()
      .getContentAsString();

    return objectMapper.readTree(body).get("token").asText();
  }

  private long createNote(String token, String title, String description) throws Exception {
    String body = mvc.perform(post("/api/notes")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json(Map.of("title", title, "description", description))))
      .andExpect(status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString();

    JsonNode root = objectMapper.readTree(body);
    return root.get("data").get("id").asLong();
  }

  private String json(Object value) throws Exception {
    return objectMapper.writeValueAsString(value);
  }
}
