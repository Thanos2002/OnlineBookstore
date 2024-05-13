package com.example.onlinebookstore.formsdata;

import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.service.Strategy.ExactSearchStrategy;

import java.util.List;

public class SearchFormData {
    private String title;
    private List<BookAuthor> authors;

    private ExactSearchStrategy exactSearchStrategy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<BookAuthor> authors) {
        this.authors = authors;
    }

}
