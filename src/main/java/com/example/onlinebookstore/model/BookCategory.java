package com.example.onlinebookstore.model;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name="bookCategory")
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="categoryid")
    private int categoryid;

    @Column(name="categoryname", unique=true)
    private String categoryname;

    @OneToMany(mappedBy = "bookCategory",fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Book> books;

    @ManyToMany(mappedBy="favouriteCategories")
    private List<UserProfile> userProfilesCategories;

/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCategory that = (BookCategory) o;
        return categoryid == that.categoryid &&
                Objects.equals(categoryname, that.categoryname);
    }
    @Override
    public int hashCode() {
        return Objects.hash(categoryid, categoryname);
    }

 */

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<UserProfile> getUserProfilesCategories() {
        return userProfilesCategories;
    }

    public void setUserProfilesCategories(List<UserProfile> userProfilesCategories) {
        this.userProfilesCategories = userProfilesCategories;
    }
}
