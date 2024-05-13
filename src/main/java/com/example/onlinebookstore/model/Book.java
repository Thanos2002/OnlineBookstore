package com.example.onlinebookstore.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bookid")
    private int bookid;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="bookcategory_id")
    private BookCategory bookCategory;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade=CascadeType.ALL)
    @JoinTable(
            name = "books_Authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<BookAuthor> bookAuthors;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id")
    private UserProfile profileThatOffers;

    @ManyToMany(mappedBy = "requestedBooks",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserProfile> requestingUsers;


    // Method to remove a BookAuthor from the bookAuthors list
    public void removeBookAuthor(BookAuthor bookAuthor) {
        if (bookAuthors != null) {
            bookAuthors.remove(bookAuthor);
            if (bookAuthor != null) {
                bookAuthor.getBooks().remove(this);
            }
        }
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
