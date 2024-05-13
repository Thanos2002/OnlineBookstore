package com.example.onlinebookstore.service.Strategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;


import java.util.List;

public class ApproximateSearchStrategy extends TemplateSearchStrategy {

    private BookDAO bookDAO;

    @Override
    protected List<Book> makeInitialListOfBooks(SearchFormData searchFormData) {
        String title = searchFormData.getTitle();
        return bookDAO.findByTitleContaining(title); // Implement method in BookDAO (full-text search)
    }

    @Override
    protected boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
        List<BookAuthor> searchAuthors = searchFormData.getAuthors(); // Assuming a list of author names
        List<BookAuthor> bookAuthors = book.getBookAuthors(); // Assuming a list of author names in Book

        // Check if all search authors are present in book authors (case-insensitive)
        for (BookAuthor searchAuthor : searchAuthors) {
            boolean found = false;
            for (BookAuthor bookAuthor : bookAuthors) {
                if (searchAuthor.equals(bookAuthor)) {
                    found = true;
                    break;
                }
            }
            if (!found) {return false;}
        }return true;
    }
}
