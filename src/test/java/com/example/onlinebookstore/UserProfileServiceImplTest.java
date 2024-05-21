package com.example.onlinebookstore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.onlinebookstore.service.UserProfile.UserProfileServiceImpl;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.onlinebookstore.dao.UserprofileDAO;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.*;
import com.example.onlinebookstore.model.*;

public class UserProfileServiceImplTest {

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Mock
    private UserprofileDAO userprofileDAO;

    @Mock
    private BookDAO bookDAO;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransformData() {
        UserProfileFormData formData = new UserProfileFormData();
        formData.setId(1);
        formData.setUsername("testuser");
        formData.setPhoneNumber("1234567890");
        formData.setFullName("Test User");
        formData.setAge(25);
        formData.setFavouriteAuthors(new ArrayList<>());
        formData.setFavouriteCategories(new ArrayList<>());
        formData.setRequestedBooks(new ArrayList<>());
        formData.setBookOffers(new ArrayList<>());

        when(userprofileDAO.findById(1)).thenReturn(Optional.of(new UserProfile()));
        when(entityManager.find(eq(BookAuthor.class), anyInt())).thenReturn(new BookAuthor());
        when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(new BookCategory());

        UserProfile userProfile = userProfileService.transformData(formData);

        assertNotNull(userProfile);
        assertEquals(1, userProfile.getId());
        assertEquals("testuser", userProfile.getUsername());
        assertEquals("1234567890", userProfile.getPhone_number());
        assertEquals("Test User", userProfile.getFull_name());
        assertEquals(25, userProfile.getAge());
        verify(userprofileDAO, times(1)).findById(1);
    }

    @Test
    void testRetrieveProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(1);
        userProfile.setUsername("testuser");

        when(userprofileDAO.findById(1)).thenReturn(Optional.of(userProfile));

        UserProfileFormData formData = userProfileService.retrieveProfile(1);

        assertNotNull(formData);
        assertEquals(1, formData.getId());
        assertEquals("testuser", formData.getUsername());
        verify(userprofileDAO, times(1)).findById(1);
    }

    @Test
    void testSave() {
        UserProfileFormData formData = new UserProfileFormData();
        formData.setId(1);
        formData.setUsername("testuser");
        formData.setFavouriteAuthors(new ArrayList<>());
        formData.setFavouriteCategories(new ArrayList<>());
        formData.setRequestedBooks(new ArrayList<>());
        formData.setBookOffers(new ArrayList<>());

        UserProfile userProfile = new UserProfile();
        when(userprofileDAO.findById(1)).thenReturn(Optional.of(userProfile));
        when(entityManager.find(eq(BookAuthor.class), anyInt())).thenReturn(new BookAuthor());
        when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(new BookCategory());

        userProfileService.save(formData);

        verify(userprofileDAO, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testRetrieveBookOffers() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");
        userProfile.setBookOffers(new ArrayList<>());

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));

        List<BookFormData> result = userProfileService.retrieveBookOffers("testuser");

        assertNotNull(result);
        verify(userprofileDAO, times(1)).findByUsername("testuser");
    }

    @Test
    void testRetrieveSearchedBooks() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));

        List<BookFormData> bookFormDataList = new ArrayList<>();
        BookFormData bookFormData = new BookFormData();
        bookFormData.setUser_profile(new UserProfile());
        bookFormDataList.add(bookFormData);

        List<BookFormData> result = userProfileService.retrieveSearchedBooks(bookFormDataList, "testuser");

        assertNotNull(result);
        verify(userprofileDAO, times(1)).findByUsername("testuser");
    }

    @Test
    void testRetrieveBookOffersAll() {
        UserProfile ownUserprofile = new UserProfile();
        ownUserprofile.setUsername("testuser");
        ownUserprofile.setBookOffers(new ArrayList<>());

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(ownUserprofile));

        List<UserProfile> userProfiles = new ArrayList<>();
        UserProfile otherUserProfile = new UserProfile();
        otherUserProfile.setUsername("otheruser");
        otherUserProfile.setBookOffers(new ArrayList<>());
        userProfiles.add(otherUserProfile);

        when(userprofileDAO.findAll()).thenReturn(userProfiles);

        List<BookFormData> result = userProfileService.retrieveBookOffersAll("testuser");

        assertNotNull(result);
        verify(userprofileDAO, times(1)).findByUsername("testuser");
        verify(userprofileDAO, times(1)).findAll();
    }

    @Test
    void testAddBookOffer() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");
        userProfile.setBookOffers(new ArrayList<>());

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));
        when(entityManager.find(eq(BookCategory.class), anyInt())).thenReturn(new BookCategory());
        when(entityManager.find(eq(BookAuthor.class), anyInt())).thenReturn(new BookAuthor());

        BookFormData bookFormData = new BookFormData();
        bookFormData.setBookAuthors(new ArrayList<>());
        bookFormData.setBookCategory(new BookCategory());

        userProfileService.addBookOffer("testuser", bookFormData);

        verify(bookDAO, times(1)).save(any(Book.class));
        verify(userprofileDAO, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testRequestBook() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");
        userProfile.setRequestedBooks(new ArrayList<>());

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));
        when(bookDAO.findByBookid(anyInt())).thenReturn(new Book());

        userProfileService.requestBook(1, "testuser");

        verify(userprofileDAO, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testRetrieveBookRequests() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");
        userProfile.setRequestedBooks(new ArrayList<>());

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));

        List<BookFormData> result = userProfileService.retrieveBookRequests("testuser");

        assertNotNull(result);
        verify(userprofileDAO, times(1)).findByUsername("testuser");
    }

    @Test
    void testRetrieveRequestingUsers() {
        Book book = new Book();
        book.setRequestingUsers(new ArrayList<>());

        when(bookDAO.findById(anyInt())).thenReturn(Optional.of(book));

        List<UserProfileFormData> result = userProfileService.retrieveRequestingUsers(1);

        assertNotNull(result);
        verify(bookDAO, times(1)).findById(1);
    }

    @Test
    void testDeleteBookOffers() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");
        userProfile.setBookOffers(new ArrayList<>());

        Book book = new Book();

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));
        when(bookDAO.findByBookid(anyInt())).thenReturn(book);

        userProfileService.deleteBookOffers("testuser", 1);

        verify(userprofileDAO, times(1)).save(any(UserProfile.class));
    }

    @Test
    void testDeleteBookRequest() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("testuser");
        userProfile.setRequestedBooks(new ArrayList<>());

        Book book = new Book();

        when(userprofileDAO.findByUsername("testuser")).thenReturn(Optional.of(userProfile));
        when(bookDAO.findByBookid(anyInt())).thenReturn(book);

        userProfileService.deleteBookRequest("testuser", 1);

        verify(userprofileDAO, times(1)).save(any(UserProfile.class));
    }



}
