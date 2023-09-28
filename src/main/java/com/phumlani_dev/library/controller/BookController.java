package com.phumlani_dev.library.controller;

import com.phumlani_dev.library.dto.BookDTO;
import com.phumlani_dev.library.model.Book;
import com.phumlani_dev.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<?> saveBook(@RequestParam("coverImage") MultipartFile coverImage,
                                      @RequestParam("title") String title,
                                      @RequestParam("author") String author,
                                      @RequestParam("description") String description) throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle(title);
        bookDTO.setAuthor(author);
        bookDTO.setDescription(description);
        bookDTO.setCoverImage(coverImage.getBytes());

        bookService.saveBook(bookDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> getBook = bookService.getAllBook();
        return new ResponseEntity<>(getBook, HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("query") String query) {
        return bookService.searchBooks(query);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable Long bookId) throws Exception {
//        Optional<Book> book = bookService.getBookById(bookId);
        return new ResponseEntity<>(bookService.getBookById(bookId), HttpStatus.OK);
    }

    @PutMapping("/book/{bookId}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable Long bookId) throws Exception {
        Book updatedBook = bookService.updateBook(book, bookId);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<?> deleteById(@PathVariable Long bookId) throws Exception {
        bookService.deleteBookById(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
