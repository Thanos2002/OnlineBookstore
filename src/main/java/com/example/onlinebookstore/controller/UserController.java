package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.dao.BookAuthorDAO;
import com.example.onlinebookstore.dao.BookCategoryDAO;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.UserProfileFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.model.BookCategory;
import com.example.onlinebookstore.model.User;
import com.example.onlinebookstore.service.UserProfile.UserProfileService;
import com.example.onlinebookstore.service.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    @RequestMapping("/user/main_menu")
    public String getUserMainMenu(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.err.println(currentUsername);
        // Retrieve the user entity using the username
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            model.addAttribute("userProfile", userProfile);
        }

        return "/user/main_menu";
    }

    @RequestMapping( "user/retrieveProfile")
    public String retrieveProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.err.println(currentUsername);
        // Retrieve the user entity using the username
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            model.addAttribute("userProfile", userProfile);
        }
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
    public String saveProfile(@ModelAttribute("userprofile") UserProfileFormData userProfileFormData,Model model,@RequestParam("favouriteCategories") List<BookCategory> favouriteCategories){
        try {
            userProfileService.save(userProfileFormData);
            model.addAttribute("successMessage","Profile edited successfully!");
            return "/auth/signin";
        } catch (Exception e) {
            model.addAttribute("errorMessage","Error: Unable to save profile information!");
            logger.error("Error saving profile: {}", e.getMessage(), e);
            model.addAttribute("authors", bookAuthorDAO.findAll());
            model.addAttribute("categories",bookCategoryDAO.findAll());
            return "/user/profile";
           //return "redirect:/login?saveError=true";
        }
    }

    @RequestMapping("/user/list")
    public String listBookOffers(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            model.addAttribute("userprofile", userProfile);

            List<BookFormData> bookOffers = userProfileService.retrieveBookOffers(userProfile.getUsername());
            //logger.info("INFO {}",bookOffers.getFirst().getTitle());
            model.addAttribute("bookOffers",bookOffers);
        }
        return "/user/bookOffersList";
    }

    @RequestMapping("/user/listAll")
    public String listBookOffersAll(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            List<BookFormData> bookOffers = userProfileService.retrieveBookOffersAll(userProfile.getUsername());
            //logger.info("INFO {}",bookOffers.getFirst().getTitle());
            model.addAttribute("bookOffers",bookOffers);
        }
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
    public String saveOffer(@ModelAttribute("BookOffer") BookFormData bookFormData,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            userProfileService.addBookOffer(userProfile.getUsername(),bookFormData);
            logger.info("SAVE 2 {}",userProfile.getUsername());
            //userProfileService.save(userProfile);
        }

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
        logger.info("INFO SAVE AUTHOR");
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
        logger.info("DELETE 1 id {}",bookid);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findById(currentUsername);

        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            userProfileService.deleteBookOffers(userProfile.getUsername(),bookid);

            logger.info("DELETE 2 bookoffers controller {}", userProfile.getBookOffers().size());
            //userProfileService.save(userProfile);
            logger.info("DELETE 3 {}",userProfile.getBookOffers().size());

        }
        return "redirect:/user/list";
    }

    @RequestMapping("/user/requestBook")
    public String requestBook(@RequestParam("book") int bookid,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            try {
                UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
                model.addAttribute("userProfile",userProfile);
                userProfileService.requestBook(bookid,userProfile.getUsername());
                model.addAttribute("successMessage","Requested successfully!");
            }catch (Exception e){
                model.addAttribute("successMessage","Book already requested!");
                logger.error("Error saving profile: {}", e.getMessage(), e);
            }
        }return "/user/main_menu";
    }

    @RequestMapping("/user/deleteRequest")
    public String deleteRequest(@RequestParam("book") int bookid, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.err.println(currentUsername);
        // Retrieve the user entity using the username
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            model.addAttribute("userProfile", userProfile);
            userProfileService.deleteBookRequest(userProfile.getUsername(),bookid);
        }
        return "redirect:/user/bookRequests";
    }

    @RequestMapping("/user/bookRequests")
    public String showUserBookRequests(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.err.println(currentUsername);
        // Retrieve the user entity using the username
        Optional<User> currentUser = userService.findById(currentUsername);
        if (currentUser.isPresent()) {
            UserProfileFormData userProfile = userProfileService.retrieveProfile(currentUser.get().getId());
            model.addAttribute("userProfile", userProfile);
            List<BookFormData> bookRequests = userProfileService.retrieveBookRequests(userProfile.getUsername());
            model.addAttribute("bookRequests",bookRequests);
        }
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

    @RequestMapping("/offers2")
    public String deleteBookOffer(String username , int bookid , Model model){
        userProfileService.deleteBookOffers(username,bookid);
        model.addAttribute("bookOffer",bookid);
        return "/user/main_menu";
    }

    @RequestMapping("/request_form3")
    public String deleteBookRequest(String username ,int bookid, Model model){
        userProfileService.deleteBookRequest(username,bookid);
        model.addAttribute("requestedBook",bookid);
        return "/user/main_menu";
    }

    @RequestMapping("/search_form")
    public String showSearchForm(Model model){
        return "/search_form";
    }


}
