package com.phumlani_dev.library;

import com.phumlani_dev.library.dto.BookDTO;
import com.phumlani_dev.library.model.Book;
import com.phumlani_dev.library.repository.BookRepository;
import com.phumlani_dev.library.service.BookService;
import com.phumlani_dev.library.service.FirebaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Slf4j
class BookServiceTest {

    private BookService bookService;
    private BookRepository bookRepository;
    private FirebaseService firebaseService;

    @BeforeEach
    public void setUp() {
        bookRepository = mock(BookRepository.class);
        firebaseService = mock(FirebaseService.class);
        bookService = new BookService(bookRepository, firebaseService);
    }

    @Test
    public void testSaveBook() throws Exception {
        // Create a sample BookDTO and a mocked image URL
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setDescription("Test Description");
        String imageUrl = "https://example.com/image.jpg";

        // Mock the behavior of bookRepository and firebaseService
        when(bookRepository.findBookByTitle("Test Book")).thenReturn(Optional.empty());
        when(firebaseService.uploadImage(any())).thenReturn(imageUrl);

        // Call the method under test
        bookService.saveBook(bookDTO);

        // Verify that bookRepository.save() was called once
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    public void testGetBookById() throws Exception {
        long bookId = 1L;
        Book book = new Book();
        book.setBookId(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(bookId);

        assertTrue(result.isPresent());
        assertEquals(bookId, result.get().getBookId());
    }

    @Test
    public void testGetBookByIdNotFound() {
        long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> bookService.getBookById(bookId));
    }

    @Test
    public void testUpdateBook() throws Exception {
        long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setBookId(bookId);

        Book updatedBook = new Book();
        updatedBook.setBookId(bookId);
        updatedBook.setTitle("Updated Title");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any())).thenReturn(updatedBook);

        Book result = bookService.updateBook(updatedBook, bookId);

        assertEquals(updatedBook.getBookId(), result.getBookId());
        assertEquals(updatedBook.getTitle(), result.getTitle());
    }

    @Test
    public void testUpdateBookNotFound() {
        long bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setBookId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> bookService.updateBook(updatedBook, bookId));
    }

    @Test
    public void testDeleteBookById() throws Exception {
        long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        bookService.deleteBookById(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testDeleteBookByIdNotFound() {
        long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> bookService.deleteBookById(bookId));
    }

    @Test
    public void testSearchBooks() {
        String query = "Test";
        List<Book> mockBooks = new ArrayList<>();
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query))
                .thenReturn(mockBooks);

        List<Book> result = bookService.searchBooks(query);

        assertEquals(mockBooks, result);
    }

}
