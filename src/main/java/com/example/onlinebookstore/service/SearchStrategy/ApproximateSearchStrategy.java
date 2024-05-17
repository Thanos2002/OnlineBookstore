package com.example.onlinebookstore.service.SearchStrategy;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ApproximateSearchStrategy extends TemplateSearchStrategy {

    @Override
    protected List<Book> makeInitialListOfBooks(SearchFormData searchFormData,BookDAO bookDAO) {
        String title = searchFormData.getTitle();
        return bookDAO.findByTitleContaining(title); // Implement method in BookDAO (full-text search)
    }

    @Override
    protected boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
        List<BookAuthor> searchAuthors = searchFormData.getBookAuthors(); // Assuming a list of author names
        List<BookAuthor> bookAuthors = book.getBookAuthors(); // Assuming a list of author names in Book

        if (searchAuthors == null || bookAuthors == null) {
            return false;
        }
        // Check if all search authors are present in book authors
        for (BookAuthor searchAuthor : searchAuthors) {
            boolean found = false;
            for (BookAuthor bookAuthor : bookAuthors) {
                if (searchAuthor.getAuthorName().equalsIgnoreCase(bookAuthor.getAuthorName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
