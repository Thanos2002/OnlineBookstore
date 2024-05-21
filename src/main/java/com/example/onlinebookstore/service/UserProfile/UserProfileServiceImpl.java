package com.example.onlinebookstore.service.UserProfile;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.*;
import com.example.onlinebookstore.formsdata.*;
import com.example.onlinebookstore.model.*;

import com.example.onlinebookstore.service.RecommendationsStrategy.AuthorsRecommendationsStrategy;
import com.example.onlinebookstore.service.RecommendationsStrategy.CategoriesRecommendationsStrategy;
import com.example.onlinebookstore.service.RecommendationsStrategy.RecommendationsStrategy;
import com.example.onlinebookstore.service.SearchStrategy.ApproximateSearchStrategy;
import com.example.onlinebookstore.service.SearchStrategy.ExactSearchStrategy;
import com.example.onlinebookstore.service.SearchStrategy.SearchStrategy;
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
    private UserprofileDAO userprofileDAO;

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
        logger.info("transform data {}: ",userProfileFormData.getId());
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
        if (byUsername.isPresent()) {
            // Handle case when user profile with the given username does not exist
            UserProfile userProfile = byUsername.get();
            // Retrieve the list of book offers associated with the found UserProfile
            List<Book> bookOffers = userProfile.getBookOffers();
            // Convert the list of book offers from entities to form data objects (BookFormData)
            List<BookFormData> bookFormDataList = new ArrayList<>();
            for (Book bookOffer : bookOffers) {
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
            return bookFormDataList;
        }
        return null;
    }

    public List<BookFormData> retrieveSearchedBooks(List<BookFormData> bookFormData, String username){
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            UserProfile ownUserprofile = byUsername.get();
            List<BookFormData> results = new ArrayList<>();
            for (BookFormData book : bookFormData) {
                if (book.getUser_profile() != null && book.getUser_profile().getUsername() != null) {
                    String bookOwnerUsername = book.getUser_profile().getUsername();
                    // Retrieve all the book offers that aren't yours
                    if (!bookOwnerUsername.equals(ownUserprofile.getUsername())) {
                        results.add(book);
                    }
                }
            }return results;
        }
        return null;
    }

    @Override
    public List<BookFormData> retrieveBookOffersAll(String username) {
        // Find the UserProfile entity corresponding to the given username
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        if (byUsername.isPresent()) {
            UserProfile ownUserprofile = byUsername.get();
            List<UserProfile> userProfiles = userprofileDAO.findAll();
            List<BookFormData> bookFormDataList = new ArrayList<>();
            for (UserProfile userProfile : userProfiles) {
                // Retrieve all the book offers that aren't yours
                if (!userProfile.getUsername().equals(ownUserprofile.getUsername())) {
                    List<Book> bookOffers = userProfile.getBookOffers();
                    for (Book bookOffer : bookOffers) {
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
        }
        return null;
    }


    @Transactional
    @Override
    public void addBookOffer(String username, BookFormData book) {
        Optional<UserProfile> byUsername = userprofileDAO.findByUsername(username);
        Book newbook = new Book();
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
        if (byUsername.isPresent()) {
            UserProfile userProfile = byUsername.get();
            Book book = bookDAO.findByBookid(bookid);
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
            // Retrieve the list of book offers associated with the found UserProfile
            List<Book> bookRequests = userProfile.getRequestedBooks();
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
                // Save the updated UserProfile entity
                userprofileDAO.save(userProfile);
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

    @Override
    public List<BookFormData> searchBooks(SearchFormData searchFormData){
        List<BookFormData> searchResults,finalResults;
        SearchStrategy strategy;
        if(searchFormData.getSearchStrategy().equals("ApproximateSearchStrategy")){
            strategy = new ApproximateSearchStrategy();
        }else if(searchFormData.getSearchStrategy().equals("ExactSearchStrategy")){
            strategy = new ExactSearchStrategy();
        }else{
            strategy = new ApproximateSearchStrategy();
        }
        searchResults = strategy.search(searchFormData,bookDAO);
        finalResults = retrieveSearchedBooks(searchResults,searchFormData.getUsername());
        return finalResults;
    }
    @Override
    public List<BookFormData> reacommendBooks(RecommendationsFormData recommendationsFormData){
        List<BookFormData> searchResults,finalResults;
        RecommendationsStrategy strategy;
        if(recommendationsFormData.getRecommendationsStrategy().equals("CategoriesRecommendationsStrategy")){
            strategy = new CategoriesRecommendationsStrategy();
        }else{
            strategy = new AuthorsRecommendationsStrategy();
        }
        searchResults = strategy.recommend(recommendationsFormData,bookDAO);
        finalResults = retrieveSearchedBooks(searchResults,recommendationsFormData.getUserProfileFormData().getUsername());
        return finalResults;


    }

}
