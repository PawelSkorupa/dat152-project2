/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.BookService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/books")
    public ResponseEntity<Object> getAllBooks(){
        List<Book> books = bookService.findAll();
        if(books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/books/{isbn}")
    public ResponseEntity<Object> getBook(@PathVariable("isbn") String isbn) throws BookNotFoundException {
        Book book = bookService.findByISBN(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        Book nbook = bookService.saveBook(book);
        return new ResponseEntity<>(nbook, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/books/{isbn}/authors")
    public ResponseEntity<?> getAuthorsOfBookByISBN(@PathVariable("isbn") String isbn) throws BookNotFoundException {
        return new ResponseEntity<>(bookService.findAuthorsOfBookByISBN(isbn), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/books/{isbn}")
    public ResponseEntity<Book> updateBookByISBN(@PathVariable("isbn") String isbn, @RequestBody Book book) {
        return new ResponseEntity<>(bookService.updateBook(book, isbn), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/books/{isbn}")
    public ResponseEntity<?> deleteBookByISBN(@PathVariable("isbn") String isbn) throws BookNotFoundException {
        bookService.deleteByISBN(isbn);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
