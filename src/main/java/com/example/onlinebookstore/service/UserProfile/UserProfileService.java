package com.example.onlinebookstore.service.UserProfile;

import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.formsdata.UserProfileFormData;
import com.example.onlinebookstore.model.UserProfile;

import java.util.List;

public interface UserProfileService {
    public UserProfileFormData retrieveProfile(int id);
    public void save(UserProfileFormData userProfile);
    public List<BookFormData> retrieveBookOffers(String username);
    public List<BookFormData> retrieveBookOffersAll(String username);
    public  void addBookOffer(String username , BookFormData book);
    //public  List<BookFormData> searchBooks(SearchFormData searchFormData);
    //public List<BookFormData> recommendedBooks(String username,RecommedationsFormData recommedationsFormData);
    public void requestBook(int bookid, String username);
    public List<BookFormData> retrieveBookRequests(String username);
    public List<UserProfileFormData> retrieveRequestingUsers(int bookid);
    public void deleteBookOffers(String username,int bookid);
    public void deleteBookRequest(String username ,int bookid);
    public List<BookFormData> searchBooks(SearchFormData searchFormData);


}
