package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dao.*;
import com.example.onlinebookstore.formsdata.*;
import com.example.onlinebookstore.model.*;
import com.example.onlinebookstore.service.UserProfile.UserProfileService;
import com.example.onlinebookstore.service.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserService userService;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    BookAuthorDAO bookAuthorDAO;
    @Autowired
    BookCategoryDAO bookCategoryDAO;
    @Autowired
    BookDAO bookDAO;
    @Autowired
    userDAO userDAO;

    public UserProfileFormData findUserProfile(){   // Helper method
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.err.println(currentUsername);
        // Retrieve the user entity using the username
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            return userProfile;
        }
        return null;
    }

    @RequestMapping("/user/main_menu")
    public String getUserMainMenu(Model model){
        UserProfileFormData userProfile = findUserProfile();
        model.addAttribute("userProfile", userProfile);

        return "/user/main_menu";
    }

    @RequestMapping( "user/retrieveProfile")
    public String retrieveProfile(Model model){
        UserProfileFormData userProfile = findUserProfile();
        model.addAttribute("userProfile", userProfile);
        return "/user/profileInfo";
    }

    @RequestMapping("/user/updateProfile")
    public String updateProfile(@RequestParam("userProfileName") int id,Model model){
        UserProfileFormData userProfileFormData = userProfileService.retrieveProfile(id);
        model.addAttribute("userprofile",userProfileFormData);
        model.addAttribute("authors", bookAuthorDAO.findAll());
        model.addAttribute("categories",bookCategoryDAO.findAll());
        return "/user/profile";
    }

    @RequestMapping("/user/saveProfile")
    public String saveProfile(@ModelAttribute("userprofile") UserProfileFormData userProfileFormData,Model model){
        try {
            userProfileService.save(userProfileFormData);
            model.addAttribute("successMessage","Profile edited successfully!");
            return "/auth/signin";
        }catch (Exception e) {
            model.addAttribute("errorMessage","Error: Unable to save profile information!");
            logger.error("Error saving profile: {}", e.getMessage(), e);
            model.addAttribute("authors", bookAuthorDAO.findAll());
            model.addAttribute("categories",bookCategoryDAO.findAll());
            return "/user/profile";
        }
    }

    @RequestMapping("/user/list")
    public String listBookOffers(Model model){
        UserProfileFormData userProfile = findUserProfile();
        model.addAttribute("userProfile", userProfile);
        List<BookFormData> bookOffers = userProfileService.retrieveBookOffers(userProfile.getUsername());
        model.addAttribute("bookOffers",bookOffers);
        return "/user/bookOffersList";
    }

    @RequestMapping("/user/listAll")
    public String listBookOffersAll(Model model){
        UserProfileFormData userProfile = findUserProfile();
        model.addAttribute("userProfile", userProfile);
        List<BookFormData> bookOffers = userProfileService.retrieveBookOffersAll(userProfile.getUsername());
        model.addAttribute("bookOffers",bookOffers);

        return "/user/bookOffersAll";
    }

    @RequestMapping("/user/offerform")
    public String showOfferForm(Model model){
        // send over to our form
        model.addAttribute("bookOffer",new BookFormData());
        model.addAttribute("authors", bookAuthorDAO.findAll());
        model.addAttribute("categories",bookCategoryDAO.findAll());

        return "/user/bookOfferForm";
    }

    @RequestMapping("/user/saveoffer")
    public String saveOffer(@ModelAttribute("bookOffer") BookFormData bookFormData,Model model){
        UserProfileFormData userProfile = findUserProfile();
        userProfileService.addBookOffer(userProfile.getUsername(),bookFormData);

        return "redirect:/user/main_menu";
    }

    @RequestMapping("/user/addAuthor")
    public String addAuthor(Model model){
        model.addAttribute("bookAuthor",new BookAuthor());
        logger.info("INFO ADD AUTHOR");
        return "user/addAuthor";
    }

    @RequestMapping("/user/saveAuthor")
    public String saveAuthor(@ModelAttribute("bookAuthor") BookAuthor bookAuthor,Model model){
        try {
            model.addAttribute("successMessageAuthor","Author saved successfully!");
            bookAuthorDAO.save(bookAuthor);
            model.addAttribute("bookOffer",new BookFormData());
            model.addAttribute("authors", bookAuthorDAO.findAll());
            model.addAttribute("categories",bookCategoryDAO.findAll());

        } catch (Exception e) {
            model.addAttribute("errorMessage","Error: Unable to save profile information!");
            logger.error("Error saving profile: {}", e.getMessage(), e);
        }return "/user/bookOfferForm";
    }

    @RequestMapping("/user/deleteoffer")
    public String deleteOffer(@RequestParam("book") int bookid, Model model){
        UserProfileFormData userProfile = findUserProfile();
        userProfileService.deleteBookOffers(userProfile.getUsername(),bookid);
        return "redirect:/user/list";
    }

    @RequestMapping("/user/requestBook")
    public String requestBook(@RequestParam("book") int bookid,Model model){
        try {
            UserProfileFormData userProfile = findUserProfile();
            model.addAttribute("userProfile",userProfile);
            userProfileService.requestBook(bookid,userProfile.getUsername());
            model.addAttribute("successMessage","Requested successfully!");
        }catch (Exception e){
            model.addAttribute("successMessage","Book already requested!");
            logger.error("Error saving profile: {}", e.getMessage(), e);
        }
        return "/user/main_menu";
    }

    @RequestMapping("/user/deleteRequest")
    public String deleteRequest(@RequestParam("book") int bookid, Model model){
        UserProfileFormData userProfile = findUserProfile();
        model.addAttribute("userProfile", userProfile);
        userProfileService.deleteBookRequest(userProfile.getUsername(),bookid);
        return "redirect:/user/bookRequests";
    }

    @RequestMapping("/user/bookRequests")
    public String showUserBookRequests(Model model){
        UserProfileFormData userProfile = findUserProfile();
        model.addAttribute("userProfile", userProfile);
        List<BookFormData> bookRequests = userProfileService.retrieveBookRequests(userProfile.getUsername());
        model.addAttribute("bookRequests",bookRequests);
        return "/user/bookRequests";
    }

    @RequestMapping("/user/requesting_users")
    public String showRequestingUsersForBookOffers(@RequestParam("book") int bookid,Model model){
        List<UserProfileFormData> requesting_users = userProfileService.retrieveRequestingUsers(bookid);
        model.addAttribute("requestingUsers",requesting_users);
        Book book = bookDAO.findByBookid(bookid);
        model.addAttribute("requestedBook",book);
        return "/user/requestingUsers";
    }

    @RequestMapping("/request_form2")
    public String acceptRequest(String username, int bookid, Model model){
        userProfileService.requestBook(bookid,username);
        model.addAttribute("requestingUser",username);
        return "/user/main_menu";
    }

    @RequestMapping("/user/showSearchForm")
    public String showSearchForm(Model model){
        model.addAttribute("bookauthors", bookAuthorDAO.findAll());
        model.addAttribute("searchFormData",new SearchFormData());
        return "/user/searchForm";
    }
    @RequestMapping("/user/search")
    public String search(@ModelAttribute("searchFormData") SearchFormData searchFormData, Model model){
        UserProfileFormData userProfile = findUserProfile();
        searchFormData.setUsername(userProfile.getUsername());
        List<BookFormData> results = userProfileService.searchBooks(searchFormData);
        model.addAttribute("books", results);
        return "/user/showSearchResults";
    }
    @RequestMapping("/user/showRecommendationsForm")
    public String showRecommendationsForm(Model model){
        model.addAttribute("recommendationsFormData",new RecommendationsFormData());
        return "/user/showRecommendationsForm";
    }
    @RequestMapping("/user/recommendations")
    public String recommendations(@ModelAttribute("recommendationsFormData") RecommendationsFormData recommendationsFormData,Model model){
        UserProfileFormData userProfile = findUserProfile();
        recommendationsFormData.setUserProfileFormData(userProfile);
        List<BookFormData> results = userProfileService.reacommendBooks(recommendationsFormData);
        model.addAttribute("books", results);
        return "/user/showRecommendations";
    }


}
