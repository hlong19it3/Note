package com.example.sqlapi.repository;

import com.example.sqlapi.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
