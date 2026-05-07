package com.sailesh.notes.notes;

import com.sailesh.notes.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {
  Page<Note> findByUser(User user, Pageable pageable);

  @Query("""
    select n from Note n
    where n.user = :user
      and (
        lower(n.title) like lower(concat('%', :query, '%'))
        or lower(coalesce(n.description, '')) like lower(concat('%', :query, '%'))
      )
    """)
  Page<Note> searchByUser(@Param("user") User user, @Param("query") String query, Pageable pageable);
}
