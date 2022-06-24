package com.example.sqlapi.controller;

import com.example.sqlapi.ResourceNotFoundException;
import com.example.sqlapi.model.Note;
import com.example.sqlapi.repository.NoteRepository;
import org.hibernate.EntityMode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    //Basic API
    @GetMapping("/notes")
    public List<Note> getAllNotes1() {
        List<Note> notes = noteRepository.findAll();
        return notes;
    }
//    API root resource
    @GetMapping("/notes2")
    CollectionModel<EntityModel<Note>> all(){
        List<EntityModel<Note>> notes = noteRepository.findAll().stream().map(note -> EntityModel.of(note,
                linkTo(methodOn(NoteController.class).one(note.getId())).withSelfRel(),
                linkTo(methodOn(NoteController.class).all()).withRel("notes"))).collect(Collectors.toList());
        return CollectionModel.of(notes,linkTo(methodOn(NoteController.class).all()).withSelfRel());

    }
    // restful
    @GetMapping("/notes2/{id}")
    EntityModel<Note> one(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", id));

        return EntityModel.of(note, //
                linkTo(methodOn(NoteController.class).one(id)).withSelfRel(),
                linkTo(methodOn(NoteController.class).all()).withRel("notes"));
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
