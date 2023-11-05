package com.phumlani_dev.library.service;

import com.phumlani_dev.library.dto.BookDTO;
import com.phumlani_dev.library.model.Book;
import com.phumlani_dev.library.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
    public void shouldSaveBook() throws Exception {
        // Create a sample BookDTO and a mocked image URL
        String imageUrl = "https://example.com/image.jpg";
        BookDTO bookDTO = new BookDTO("Test Book", "Test Author", "Test Description", imageUrl.getBytes());
//        bookDTO.setTitle();
//        bookDTO.setAuthor();
//        bookDTO.setDescription();

        // Mock the behavior of bookRepository and firebaseService
        when(bookRepository.findBookByTitle("Test Book")).thenReturn(Optional.empty());
        when(firebaseService.uploadImage(any())).thenReturn(imageUrl);

        // Call the method under test
        bookService.saveBook(bookDTO);

        // Verify that bookRepository.save() was called once
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    public void shouldThrowExceptionIfBookExists(){

        // Arrange
        String imageUrl = "https://example.com/image.jpg";
        BookDTO bookDTO = new BookDTO("Test Book", "Test Author", "Test Description", imageUrl.getBytes());

        Mockito.when(bookRepository.findBookByTitle(bookDTO.getTitle())).thenReturn(Optional.of(new Book()));

        // Expect exception to be thrown
        assertThrows(Exception.class, () -> bookService.saveBook(bookDTO));
    }

    @Test
    public void shouldGetAllBooks(){

        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book(1L, "Book 1", "Author 1", "Description 1", "cover-image-1"));
        mockBooks.add(new Book(2L, "Book 2", "Author 2", "Description 2", "cover-image-2"));

        Mockito.when(bookRepository.findAll()).thenReturn(mockBooks);

        List<Book> result = bookService.getAllBook();

        assertEquals(mockBooks.size(), result.size());
        for (int i = 0; i < mockBooks.size(); i++) {
            Book mockBook = mockBooks.get(i);
            Book resultBook = result.get(i);

            assertEquals(mockBook.getBookId(), resultBook.getBookId());
            assertEquals(mockBook.getTitle(), resultBook.getTitle());
            assertEquals(mockBook.getAuthor(), resultBook.getAuthor());
            assertEquals(mockBook.getDescription(), resultBook.getDescription());
            assertEquals(mockBook.getCoverImage(), resultBook.getCoverImage());
        }
    }

    @Test
    public void shouldGetBookById() throws Exception {
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
