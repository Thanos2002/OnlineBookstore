package com.example.onlinebookstore.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="userprofiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id; // Primary key for Userprofile
    @Column(name="user_name", unique=true)
    private String username;
    @Column(name="phone_number")
    private String phone_number;

    @Column(name="full_name")
    private String full_name;

    @Column(name="age")
    private int age;

    // Userprofile class
    @OneToMany(mappedBy = "profileThatOffers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Book> bookOffers;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "requestingUsers",
            joinColumns = @JoinColumn(name = "requesting_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"requesting_profile_id", "book_id"}))
    private List<Book> requestedBooks;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "favouriteBookAuthors",
            joinColumns = @JoinColumn(name = "userprofiles_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<BookAuthor> favouriteAuthors;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "favouriteCategories",
            joinColumns = @JoinColumn(name = "userprofiles_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<BookCategory> favouriteCategories;


    // Getter and setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void removeBook(Book book) {
        bookOffers.remove(book);
        book.setUser_profile(null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
