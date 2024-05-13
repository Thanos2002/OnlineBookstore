package com.example.onlinebookstore.service.Strategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;

import java.util.List;

public class ExactSearchStrategy extends TemplateSearchStrategy {

    private BookDAO bookDAO;
    @Override
    protected List<Book> makeInitialListOfBooks(SearchFormData searchFormData) {
        String title = searchFormData.getTitle();
        List<BookAuthor> authors = searchFormData.getAuthors(); // Assuming a list of author names
        return null;//bookDAO.findByExactTitleAndAuthors(title, authors); // Implement method in BookDAO
    }

    @Override
    protected boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
        // This method is not needed for exact search since authors are already matched in makeInitialListOfBooks
        return true;
    }
}
