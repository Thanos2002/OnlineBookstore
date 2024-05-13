package com.example.onlinebookstore.service.UserProfile;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.*;
import com.example.onlinebookstore.formsdata.*;
import com.example.onlinebookstore.model.*;

import com.example.onlinebookstore.service.User.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService{
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserprofileDAO userprofileDAO;

    @Autowired
    private BookAuthorDAO bookAuthorDAO;

    @Autowired
    private BookCategoryDAO bookCategoryDAO;

    @Autowired
    private BookDAO bookDAO;


    @Transactional
    public UserProfile transformData(UserProfileFormData userProfileFormData ){
        Optional<UserProfile> byUsername = userprofileDAO.findById(userProfileFormData.getId());
        UserProfile userProfile;
        if (byUsername.isPresent()) {
            userProfile = byUsername.get();
        }else{
            userProfile = new UserProfile();
        }
        userProfile.setId(userProfileFormData.getId());
        userProfile.setUsername(userProfileFormData.getUsername());
        userProfile.setPhone_number(userProfileFormData.getPhoneNumber());
        userProfile.setFull_name(userProfileFormData.getFullName());
        userProfile.setAge(userProfileFormData.getAge());
        userProfile.setFavouriteAuthors(fetchBookAuthors(userProfileFormData.getFavouriteAuthors()));
        userProfile.setFavouriteCategories(fetchBookCategory(userProfileFormData.getFavouriteCategories()));
        userProfile.setRequestedBooks(userProfileFormData.getRequestedBooks());
        userProfile.setBookOffers(userProfileFormData.getBookOffers());
        return userProfile;
    }
    public UserProfileFormData transformDataToForm(UserProfile userProfile ){
        UserProfileFormData userProfileFormData = new UserProfileFormData();
        userProfileFormData.setId(userProfile.getId());
        userProfileFormData.setUsername(userProfile.getUsername());
        userProfileFormData.setPhoneNumber(userProfile.getPhone_number());
        userProfileFormData.setFullName(userProfile.getFull_name());
        userProfileFormData.setAge(userProfile.getAge());
        userProfileFormData.setFavouriteAuthors(userProfile.getFavouriteAuthors());
        userProfileFormData.setFavouriteCategories(userProfile.getFavouriteCategories());
        userProfileFormData.setRequestedBooks(userProfile.getRequestedBooks());
        userProfileFormData.setBookOffers(userProfile.getBookOffers());
        return userProfileFormData;
    }

    public List<BookAuthor> fetchBookAuthors(List<BookAuthor> authors){   // Fetch fresh instances of BookAuthor
        List<BookAuthor> freshAuthors = new ArrayList<>();
        for (BookAuthor author : authors) {
            freshAuthors.add(entityManager.find(BookAuthor.class, author.getAuthorid()));
        }
        return freshAuthors;
    }

    public List<BookCategory> fetchBookCategory(List<BookCategory> categories){   // Fetch fresh instances of BookCategory
        List<BookCategory> freshCategories = new ArrayList<>();
        for (BookCategory category : categories) {
            freshCategories.add(entityManager.find(BookCategory.class, category.getCategoryid()));
        }
        return freshCategories;
    }

    public List<Book> fetchBooks(List<Book> books){   // Fetch fresh instances of BookCategory
        List<Book> freshBooks = new ArrayList<>();
        for (Book book : books) {
            freshBooks.add(entityManager.find(Book.class, book.getBookid()));
        }
        return freshBooks;
    }
    @Override
    public UserProfileFormData retrieveProfile(int id) {
        Optional<UserProfile> byUsername = userprofileDAO.findById(id);
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            return transformDataToForm(userProfile);
        }return null;
    }
    @Transactional
    @Override
    public void save(UserProfileFormData userProfileFormData) {
        // Convert UserProfileFormData to UserProfile entity
        //Save the UserProfile entity using the userprofileDAO
        UserProfile user = transformData(userProfileFormData);
        userprofileDAO.save(user);
    }

    @Override
    public List<BookFormData> retrieveBookOffers(String username) {
        // Find the UserProfile entity corresponding to the given username
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        logger.info("INFO {}",byUsername.get().getUsername());
        if (byUsername.isPresent()) {
            // Handle case when user profile with the given username does not exist
            UserProfile userProfile = byUsername.get();
            // Retrieve the list of book offers associated with the found UserProfile
            List<Book> bookOffers = userProfile.getBookOffers();
            // Convert the list of book offers from entities to form data objects (BookFormData)
            List<BookFormData> bookFormDataList = new ArrayList<>();
            logger.info("INFO inside present");
            for (Book bookOffer : bookOffers) {
                logger.info("INFO {}",bookOffer.getTitle());
                BookFormData bookFormData = new BookFormData();
                bookFormData.setTitle(bookOffer.getTitle());
                bookFormData.setDescription(bookOffer.getDescription());
                bookFormData.setBookAuthors(bookOffer.getBookAuthors());
                bookFormData.setBookCategory(bookOffer.getBookCategory());
                bookFormData.setUser_profile(bookOffer.getUser_profile());
                bookFormData.setRequestingUsers(bookOffer.getRequestingUsers());
                bookFormData.setBookid(bookOffer.getBookid());
                bookFormDataList.add(bookFormData);
            }
            //logger.info("INFO {}",bookFormDataList.getFirst().getTitle());
            return bookFormDataList;
        }logger.info("INFO null");
        return null;
    }

    @Override
    public List<BookFormData> retrieveBookOffersAll(String username) {
        // Find the UserProfile entity corresponding to the given username
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            UserProfile ownUserprofile = byUsername.get();
            List<UserProfile> userProfiles = userprofileDAO.findAll();
            logger.info("RetrieveAll ownUserprofile {}",ownUserprofile.getUsername());
            logger.info("RetrieveAll userprofiles {}", userProfiles.size());
            List<BookFormData> bookFormDataList = new ArrayList<>();
            for (UserProfile userProfile : userProfiles) {
                if (!userProfile.getUsername().equals(ownUserprofile.getUsername())) {
                    logger.info("RetrieveAll getUsername {}", userProfile.getUsername());
                    List<Book> bookOffers = userProfile.getBookOffers();
                    for (Book bookOffer : bookOffers) {
                        logger.info("RetrieveAll {}", bookOffer.getTitle());
                        BookFormData bookFormData = new BookFormData();
                        bookFormData.setTitle(bookOffer.getTitle());
                        bookFormData.setDescription(bookOffer.getDescription());
                        bookFormData.setBookAuthors(bookOffer.getBookAuthors());
                        bookFormData.setBookCategory(bookOffer.getBookCategory());
                        bookFormData.setUser_profile(bookOffer.getUser_profile());
                        bookFormData.setRequestingUsers(bookOffer.getRequestingUsers());
                        bookFormData.setBookid(bookOffer.getBookid());
                        bookFormDataList.add(bookFormData);
                    }
                }
            }return bookFormDataList;
        }logger.info("INFO null");
        return null;
    }


    @Transactional
    @Override
    public void addBookOffer(String username, BookFormData book) {
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        Book newbook = new Book();
        logger.info("ADD 1  {}",byUsername.get().getUsername());
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            // Setting the book object;
            newbook.setBookid(book.getBookid());
            newbook.setUser_profile(userProfile);
            newbook.setDescription(book.getDescription());
            newbook.setRequestingUsers(book.getRequestingUsers());
            newbook.setTitle(book.getTitle());
            // Fetch the fresh instance of the category
            BookCategory freshCategory = entityManager.find(BookCategory.class, book.getBookCategory().getCategoryid());
            newbook.setBookCategory(freshCategory);
            // Fetch fresh Authors
            newbook.setBookAuthors(fetchBookAuthors(book.getBookAuthors()));

            bookDAO.save(newbook);
            List<Book> bookOffers = userProfile.getBookOffers();
            bookOffers.add(newbook);
            userProfile.setBookOffers(bookOffers);
            userprofileDAO.save(userProfile);

        }
    }

    @Transactional
    @Override
    public void requestBook(int bookid, String username) {
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        logger.info("requestBook 1 ");
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            Book book = bookDAO.findByBookid(bookid);
            logger.info("requestBook 2 {}",book.getTitle());
            // Retrieve the list with the requested books
            List<Book> requestedBooks = userProfile.getRequestedBooks();
            // And add the new requested book
            requestedBooks.add(book);
            userProfile.setRequestedBooks(requestedBooks);
            // Save the updated UserProfile entity
            userprofileDAO.save(userProfile);
        }
    }

    @Override
    public List<BookFormData> retrieveBookRequests(String username) {
        // Find the UserProfile entity corresponding to the given username
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            logger.info("retrieveBookRequests 1");
            // Retrieve the list of book offers associated with the found UserProfile
            List<Book> bookRequests = userProfile.getRequestedBooks();
            logger.info("retrieveBookRequests  {}",bookRequests.size());
            // Convert the list of book offers from entities to form data objects (BookFormData)
            List<BookFormData> bookFormDataList = new ArrayList<>();
            for (Book bookRequest : bookRequests) {
                BookFormData bookFormData = new BookFormData();
                bookFormData.setTitle(bookRequest.getTitle());
                bookFormData.setDescription(bookRequest.getDescription());
                bookFormData.setBookAuthors(bookRequest.getBookAuthors());
                bookFormData.setBookCategory(bookRequest.getBookCategory());
                bookFormData.setUser_profile(bookRequest.getUser_profile());
                bookFormData.setRequestingUsers(bookRequest.getRequestingUsers());
                bookFormData.setBookid(bookRequest.getBookid());

                bookFormDataList.add(bookFormData);
            }
            logger.info("retrieveBookRequests  bookFormDataList {}",bookFormDataList.size());
            return bookFormDataList;
        }
        return null;
    }

    @Override
    public List<UserProfileFormData> retrieveRequestingUsers(int bookid) {
        // Retrieve the Book entity associated with the bookid
        Optional<Book> bookFound = bookDAO.findById(bookid);
        if (bookFound.isPresent()) {
            Book book = bookFound.get();
            // Retrieve the user profiles that have requested this book
            List<UserProfile> requestingUsers = book.getRequestingUsers();
            // Convert the list of requestingUsers from entities to form data objects (UserProfileFormData)
            List<UserProfileFormData> requestingUserForms = new ArrayList<>();
            for (UserProfile userProfile : requestingUsers) {
                requestingUserForms.add(transformDataToForm(userProfile));
            }return requestingUserForms;
        }
        return null;
    }


    @Transactional
    @Override
    public void deleteBookOffers(String username, int bookid) {
        // Retrieve the UserProfile associated with the username
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            // Retrieve the Book with the given bookid
            Book book = bookDAO.findByBookid(bookid);
            if (book != null) {
                // Remove the Book from the UserProfile's bookOffers list
                userProfile.removeBook(book);
                logger.info("DELETE 2 service bookoffers {}",userProfile.getBookOffers().size());
                // Save the updated UserProfile entity
                userprofileDAO.save(userProfile);
                logger.info("DELETE 3 service bookoffers {}",userProfile.getBookOffers().size());
                // Delete the Book entity
                //bookDAO.deleteByBookid(bookid);
            }
        }
    }

    @Transactional
    @Override
    public void deleteBookRequest(String username, int bookid) {
        // Retrieve the UserProfile associated with the username
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            // Retrieve the Book with the given bookid
            Optional<Book> book = bookDAO.findById(bookid);
            if (book.isPresent()) {
                Book newbook = book.get();
                // Remove the Book from the list of requested books in the UserProfile
                List<Book> requestedBooks = userProfile.getRequestedBooks();
                requestedBooks.remove(newbook);
                userProfile.setRequestedBooks(requestedBooks);
                // Save the updated UserProfile entity
                userprofileDAO.save(userProfile);
            }
        }
    }
}
