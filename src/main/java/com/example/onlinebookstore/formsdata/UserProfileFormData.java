package com.example.onlinebookstore.formsdata;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.model.BookCategory;
import com.example.onlinebookstore.model.User;
import jakarta.persistence.*;

import java.util.List;

public class UserProfileFormData {


    private int id;
    //private User user;
    private String username;
    private String phoneNumber;
    private String fullName;
    private int age;


    private List<Book> bookOffers;

    private List<Book> requestedBooks;

    private List<BookAuthor> favouriteAuthors;

    private List<BookCategory> favouriteCategories;

    public UserProfileFormData() {
    }

    public UserProfileFormData(String username, String phoneNumber, String fullName, int age) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.age = age;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for each field
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Book> getBookOffers() {
        return bookOffers;
    }

    public void setBookOffers(List<Book> bookOffers) {
        this.bookOffers = bookOffers;
    }

    public List<Book> getRequestedBooks() {
        return requestedBooks;
    }

    public void setRequestedBooks(List<Book> requestedBooks) {
        this.requestedBooks = requestedBooks;
    }

    public List<BookAuthor> getFavouriteAuthors() {
        return favouriteAuthors;
    }

    public void setFavouriteAuthors(List<BookAuthor> favouriteAuthors) {
        this.favouriteAuthors = favouriteAuthors;
    }

    public List<BookCategory> getFavouriteCategories() {
        return favouriteCategories;
    }

    public void setFavouriteCategories(List<BookCategory> favouriteCategories) {
        this.favouriteCategories = favouriteCategories;
    }
}
