package com.example.onlinebookstore.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name="book_author")
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="authorid")
    private int authorid;

    @Column(name="authorname", unique=true)
    private String authorName;

    @ManyToMany(mappedBy = "bookAuthors",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Book> books;

    @ManyToMany(mappedBy="favouriteAuthors" )
    private List<UserProfile> userProfilesAuthors;

    public BookAuthor(int authorid, String authorName) {
        this.authorid = authorid;
        this.authorName = authorName;
    }

    public BookAuthor() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAuthor that = (BookAuthor) o;
        return authorid == that.authorid &&
                Objects.equals(authorName, that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorid, authorName);
    }


    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<UserProfile> getUserProfilesAuthors() {
        return userProfilesAuthors;
    }

    public void setUserProfilesAuthors(List<UserProfile> userProfilesAuthors) {
        this.userProfilesAuthors = userProfilesAuthors;
    }

}
