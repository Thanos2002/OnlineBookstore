
package com.example.onlinebookstore;

import com.example.onlinebookstore.controller.UserController;
import com.example.onlinebookstore.formsdata.*;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.service.UserProfile.UserProfileService;
import com.example.onlinebookstore.service.User.UserService;
import com.example.onlinebookstore.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private BookAuthorDAO bookAuthorDAO;

    @Mock
    private BookCategoryDAO bookCategoryDAO;

    @Mock
    private BookDAO bookDAO;

    @Mock
    private userDAO userDAO;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRetrieveProfile() {
        // Mocking the authenticated user
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findById("testUser")).thenReturn(Optional.of(new User()));
        when(userProfileService.retrieveProfile(anyInt())).thenReturn(userProfileFormData);

        assertEquals("/user/profileInfo", userController.retrieveProfile(model));
        verify(model).addAttribute("userProfile", userProfileFormData);
    }


    @Test
    void testSearch() {
        // Mocking the authenticated user
        SearchFormData searchFormData = new SearchFormData();
        List<BookFormData> results = new ArrayList<>();
        UserProfileFormData userProfileFormData = new UserProfileFormData();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findById("testUser")).thenReturn(Optional.of(new User()));
        when(userProfileService.retrieveProfile(anyInt())).thenReturn(userProfileFormData);
        when(userProfileService.searchBooks(searchFormData)).thenReturn(results);

        assertEquals("/user/showSearchResults", userController.search(searchFormData, model));
        verify(model).addAttribute("books", results);
    }

    @Test
    void testGetUserMainMenu() {
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findById("testUser")).thenReturn(Optional.of(new User()));
        when(userProfileService.retrieveProfile(anyInt())).thenReturn(userProfileFormData);

        assertEquals("/user/main_menu", userController.getUserMainMenu(model));
        verify(model).addAttribute("userProfile", userProfileFormData);
    }


    @Test
    void testUpdateProfile() {
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        when(userProfileService.retrieveProfile(anyInt())).thenReturn(userProfileFormData);
        when(bookAuthorDAO.findAll()).thenReturn(new ArrayList<>());
        when(bookCategoryDAO.findAll()).thenReturn(new ArrayList<>());

        assertEquals("/user/profile", userController.updateProfile(1, model));
        verify(model).addAttribute("userprofile", userProfileFormData);
        verify(model).addAttribute("authors", new ArrayList<>());
        verify(model).addAttribute("categories", new ArrayList<>());
    }

    @Test
    void testSaveProfileSuccess() {
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        when(bookAuthorDAO.findAll()).thenReturn(new ArrayList<>());
        when(bookCategoryDAO.findAll()).thenReturn(new ArrayList<>());

        assertEquals("/auth/signin", userController.saveProfile(userProfileFormData, model));
        verify(model).addAttribute("successMessage", "Profile edited successfully!");
    }

    @Test
    void testSaveProfileFailure() {
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        doThrow(new RuntimeException("Error")).when(userProfileService).save(userProfileFormData);
        when(bookAuthorDAO.findAll()).thenReturn(new ArrayList<>());
        when(bookCategoryDAO.findAll()).thenReturn(new ArrayList<>());

        assertEquals("/user/profile", userController.saveProfile(userProfileFormData, model));
        verify(model).addAttribute("errorMessage", "Error: Unable to save profile information!");
        verify(model).addAttribute("authors", new ArrayList<>());
        verify(model).addAttribute("categories", new ArrayList<>());
    }

    @Test
    void testListBookOffers() {
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        List<BookFormData> bookOffers = new ArrayList<>();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findById("testUser")).thenReturn(Optional.of(new User()));
        when(userProfileService.retrieveProfile(anyInt())).thenReturn(userProfileFormData);
        when(userProfileService.retrieveBookOffers(anyString())).thenReturn(bookOffers);

        assertEquals("/user/bookOffersList", userController.listBookOffers(model));
        verify(model).addAttribute("userProfile", userProfileFormData);
        verify(model).addAttribute("bookOffers", bookOffers);
    }

    @Test
    void testListBookOffersAll() {
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        List<BookFormData> bookOffers = new ArrayList<>();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findById("testUser")).thenReturn(Optional.of(new User()));
        when(userProfileService.retrieveProfile(anyInt())).thenReturn(userProfileFormData);
        when(userProfileService.retrieveBookOffersAll(anyString())).thenReturn(bookOffers);

        assertEquals("/user/bookOffersAll", userController.listBookOffersAll(model));
        verify(model).addAttribute("userProfile", userProfileFormData);
        verify(model).addAttribute("bookOffers", bookOffers);
    }

    @Test
    void testShowOfferForm() {
        when(bookAuthorDAO.findAll()).thenReturn(new ArrayList<>());
        when(bookCategoryDAO.findAll()).thenReturn(new ArrayList<>());

        String viewName = userController.showOfferForm(model);
        assertEquals("/user/bookOfferForm", viewName);
        verify(model).addAttribute(eq("bookOffer"), any(BookFormData.class));
        verify(model).addAttribute("authors", new ArrayList<>());
        verify(model).addAttribute("categories", new ArrayList<>());
    }


}
