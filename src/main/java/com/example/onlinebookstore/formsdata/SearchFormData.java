package com.example.onlinebookstore.formsdata;

import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.model.UserProfile;
import com.example.onlinebookstore.service.Strategy.ApproximateSearchStrategy;
import com.example.onlinebookstore.service.Strategy.ExactSearchStrategy;
import com.example.onlinebookstore.service.Strategy.SearchStrategy;
import com.example.onlinebookstore.service.UserProfile.UserProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchFormData {
    private String username;
    private String title;
    private List<BookAuthor> bookAuthors;
    private String searchStrategy;
    private ApproximateSearchStrategy approximateSearchStrategy;
    private ExactSearchStrategy exactSearchStrategy;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public String getSearchStrategy() {
        return searchStrategy;
    }

    public void setSearchStrategy(String searchStrategy) {
        this.searchStrategy = searchStrategy;
    }
}
