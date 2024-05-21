package com.example.onlinebookstore;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileTest {

    private UserProfile userProfile;
    private Book book;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile();
        book = mock(Book.class);
        List<Book> bookOffers = new ArrayList<>();
        bookOffers.add(book);
        userProfile.setBookOffers(bookOffers);
    }

    @Test
    void testRemoveBook() {
        // Act
        userProfile.removeBook(book);

        // Assert
        assertFalse(userProfile.getBookOffers().contains(book), "Book should be removed from bookOffers");
        verify(book).setUser_profile(null);
    }
}
