package com.example.onlinebookstore.service.Strategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;

import java.util.ArrayList;
import java.util.List;

public abstract class TemplateSearchStrategy implements SearchStrategy {

    public List<BookFormData> search(SearchFormData searchFormData, BookDAO bookDAO){
        return null;
    }
    protected abstract List<Book> makeInitialListOfBooks(SearchFormData searchDto);
    protected abstract boolean checkIfAuthorsMatch(SearchFormData searchFormData,Book book);
}
