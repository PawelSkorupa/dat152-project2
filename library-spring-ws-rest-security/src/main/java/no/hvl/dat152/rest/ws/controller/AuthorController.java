/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/authors")
    public ResponseEntity<Object> getAllAuthor() {
        List<Author> authors = authorService.findAll();
        if(authors.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/authors/{id}")
    public ResponseEntity<Object> getAuthor(@PathVariable("id") Long id) throws AuthorNotFoundException {
        return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/authors/{id}/books")
    public ResponseEntity<?> getBooksByAuthorId(@PathVariable("id") Long id) throws AuthorNotFoundException {
        return new ResponseEntity<>(authorService.findBooksByAuthorId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/authors")
    public ResponseEntity<Object> createAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorService.saveAuthor(author), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/authors/{id}")
    public ResponseEntity<Object> updateAuthor(@PathVariable("id") int id, @RequestBody Author author) {
        return new ResponseEntity<>(authorService.updateAuthor(id, author), HttpStatus.OK);
    }
}

