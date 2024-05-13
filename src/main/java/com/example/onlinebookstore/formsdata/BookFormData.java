package com.example.onlinebookstore.formsdata;

import com.example.onlinebookstore.model.*;
import jakarta.persistence.*;
import java.util.List;


public class BookFormData {

    private int bookid;
    private String title;
    private String description;
    private BookCategory bookCategory;

    private List<BookAuthor> bookAuthors;

    private UserProfile profileThatOffers;

    private List<UserProfile> requestingUsers;

    public BookFormData(){};
    public BookFormData(int bookid, String title, BookCategory bookCategory, List<BookAuthor> bookAuthors, UserProfile profileThatOffers, List<UserProfile> requestingUsers) {
        this.bookid = bookid;
        this.title = title;
        this.bookCategory = bookCategory;
        this.bookAuthors = bookAuthors;
        this.profileThatOffers = profileThatOffers;
        this.requestingUsers = requestingUsers;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public UserProfile getUser_profile() {
        return profileThatOffers;
    }

    public void setUser_profile(UserProfile profileThatOffers) {
        this.profileThatOffers = profileThatOffers;
    }

    public List<UserProfile> getRequestingUsers() {
        return requestingUsers;
    }

    public void setRequestingUsers(List<UserProfile> requestingUsers) {
        this.requestingUsers = requestingUsers;
    }
}
