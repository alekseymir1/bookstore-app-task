package com.bookstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.repository.BookRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book(1L, "Test Book", new BigDecimal("29.99"), BookType.REGULAR);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(
                testBook,
                new Book(2L, "Another Book", new BigDecimal("19.99"), BookType.OLD_EDITION)
        );
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookService.getAllBooks();

        // Assert
        assertEquals(expectedBooks.size(), actualBooks.size());
        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_WithExistingId_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testBook, result.get());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository).findById(99L);
    }

    @Test
    void addBook_ShouldSaveAndReturnBook() {
        // Arrange
        Book newBook = new Book(null, "New Book", new BigDecimal("39.99"), BookType.NEW_RELEASE);
        Book savedBook = new Book(3L, "New Book", new BigDecimal("39.99"), BookType.NEW_RELEASE);
        when(bookRepository.save(newBook)).thenReturn(savedBook);

        // Act
        Book result = bookService.addBook(newBook);

        // Assert
        assertEquals(savedBook, result);
        verify(bookRepository).save(newBook);
    }

    @Test
    void updateBook_WithExistingId_ShouldUpdateAndReturnBook() {
        // Arrange
        Book updatedDetails = new Book(1L, "Updated Book", new BigDecimal("49.99"), BookType.NEW_RELEASE);
        Book existingBook = testBook;
        Book savedBook = new Book(1L, "Updated Book", new BigDecimal("49.99"), BookType.NEW_RELEASE);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Optional<Book> result = bookService.updateBook(1L, updatedDetails);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Book", result.get().getTitle());
        assertEquals(new BigDecimal("49.99"), result.get().getBasePrice());
        assertEquals(BookType.NEW_RELEASE, result.get().getType());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        Book updatedDetails = new Book(99L, "Updated Book", new BigDecimal("49.99"), BookType.NEW_RELEASE);
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.updateBook(99L, updatedDetails);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository).findById(99L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_WithExistingId_ShouldDeleteAndReturnTrue() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).delete(testBook);

        // Act
        boolean result = bookService.deleteBook(1L);

        // Assert
        assertTrue(result);
        verify(bookRepository).findById(1L);
        verify(bookRepository).delete(testBook);
    }

    @Test
    void deleteBook_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        boolean result = bookService.deleteBook(99L);

        // Assert
        assertFalse(result);
        verify(bookRepository).findById(99L);
        verify(bookRepository, never()).delete(any(Book.class));
    }
}