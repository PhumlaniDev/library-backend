package com.phumlani_dev.library.service;


import com.phumlani_dev.library.dto.BookDTO;
import com.phumlani_dev.library.model.Book;
import com.phumlani_dev.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service

@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    private final FirebaseService firebaseService;

    public void saveBook(BookDTO bookDTO) throws Exception {
        Optional<Book> existingBook = bookRepository.findBookByTitle(bookDTO.getTitle());
        if (existingBook.isPresent()) {
            throw new Exception("Book: " + bookDTO.getTitle() + " already exists");
        } else {
            // Save the cover image to Google Cloud Storage and get its URL
            String imageUrl = firebaseService.uploadImage(bookDTO.getCoverImage());

            Book book = new Book();
            book.setTitle(bookDTO.getTitle());
            book.setAuthor(bookDTO.getAuthor());
            book.setDescription(bookDTO.getDescription());
            book.setCoverImage(imageUrl); // Save the Firebase Storage URL as bytes

            bookRepository.save(book);
        }
    }

    public Optional<Book> getBookById(Long bookId) throws Exception {
        Optional<Book> existingBook = bookRepository.findById(bookId);
        if (existingBook.isPresent()) {
            return bookRepository.findById(bookId);
        } else {
            throw new Exception("Book not found");
        }
    }

    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    public Book updateBook(Book book, Long bookId) throws Exception {
        Optional<Book> foundBook = bookRepository.findById(bookId);
        if (foundBook.isPresent()) {
            Book existingBook = foundBook.get();
            existingBook.setAuthor(book.getAuthor());
            existingBook.setTitle(book.getTitle());
            existingBook.setDescription(book.getDescription());
            return bookRepository.save(existingBook);
        } else {
            throw new Exception("Book does not exist");
        }
    }

    public void deleteBookById(Long bookId) throws Exception {
        Optional<Book> existingBook = bookRepository.findById(bookId);
        if (existingBook.isPresent()) {
            bookRepository.deleteById(bookId);
        } else {
            throw new Exception("Book not found");
        }
    }

    public List<Book> searchBooks(String query) {
        // You can modify this method to take additional parameters for filtering.
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }
}
