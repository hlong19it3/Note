package com.example.sqlapi.controller;

import com.example.sqlapi.ResourceNotFoundException;
import com.example.sqlapi.model.Note;
import com.example.sqlapi.repository.NoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {
    final
    NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Get All Notes
    @GetMapping("/getnotes")
    public ModelAndView getAllNotes() {
        ModelAndView modelAndView = new ModelAndView();
        Long a= Long.valueOf(1);
        List<Note> notes = noteRepository.findAll();
        modelAndView.addObject("notes", notes);
        modelAndView.setViewName("va");
        return modelAndView;
    }

    @RequestMapping("/details/{id}")
    public ModelAndView getNoteByIdView (@PathVariable(value = "id") Long noteId) {
        ModelAndView modelAndView = new ModelAndView();
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
        modelAndView.addObject("note", note);
        modelAndView.setViewName("va2");
        return modelAndView;
    }


    // Create a new Note
    @PostMapping("/createnotes")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteRepository.save(note);
    }

    // Get a Single Note
    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    // Update a Note
    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                           @Valid @RequestBody Note noteDetails) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(note);
        return updatedNote;
    }

    // Delete a Note
    @DeleteMapping("/deletenotes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        noteRepository.delete(note);

        return ResponseEntity.ok().build();
    }
}
