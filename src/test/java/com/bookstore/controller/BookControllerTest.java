package com.bookstore.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bookstore.model.Book;
import com.bookstore.model.BookType;
import com.bookstore.service.BookService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

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
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookController.getAllBooks();

        // Assert
        assertEquals(expectedBooks.size(), actualBooks.size());
        assertEquals(expectedBooks, actualBooks);
        verify(bookService).getAllBooks();
    }

    @Test
    void getBookById_WithExistingId_ShouldReturnBook() {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        // Act
        ResponseEntity<Book> response = bookController.getBookById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testBook, response.getBody());
        verify(bookService).getBookById(1L);
    }

    @Test
    void getBookById_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(bookService.getBookById(99L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.getBookById(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).getBookById(99L);
    }

    @Test
    void addBook_ShouldReturnCreatedBook() {
        // Arrange
        Book newBook = new Book(null, "New Book", new BigDecimal("39.99"), BookType.NEW_RELEASE);
        Book savedBook = new Book(3L, "New Book", new BigDecimal("39.99"), BookType.NEW_RELEASE);
        when(bookService.addBook(newBook)).thenReturn(savedBook);

        // Act
        ResponseEntity<Book> response = bookController.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBook, response.getBody());
        verify(bookService).addBook(newBook);
    }

    @Test
    void updateBook_WithExistingId_ShouldReturnUpdatedBook() {
        // Arrange
        Book updatedDetails = new Book(1L, "Updated Book", new BigDecimal("49.99"), BookType.NEW_RELEASE);
        when(bookService.updateBook(1L, updatedDetails)).thenReturn(Optional.of(updatedDetails));

        // Act
        ResponseEntity<Book> response = bookController.updateBook(1L, updatedDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDetails, response.getBody());
        verify(bookService).updateBook(1L, updatedDetails);
    }

    @Test
    void updateBook_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        Book updatedDetails = new Book(99L, "Updated Book", new BigDecimal("49.99"), BookType.NEW_RELEASE);
        when(bookService.updateBook(99L, updatedDetails)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.updateBook(99L, updatedDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).updateBook(99L, updatedDetails);
    }

    @Test
    void deleteBook_WithExistingId_ShouldReturnNoContent() {
        // Arrange
        when(bookService.deleteBook(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService).deleteBook(1L);
    }

    @Test
    void deleteBook_WithNonExistingId_ShouldReturnNotFound() {
        // Arrange
        when(bookService.deleteBook(99L)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(99L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService).deleteBook(99L);
    }
}