package com.example.onlinebookstore.service.Strategy;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Component
public class ApproximateSearchStrategy extends TemplateSearchStrategy {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Override
    protected List<Book> makeInitialListOfBooks(SearchFormData searchFormData,BookDAO bookDAO) {
        String title = searchFormData.getTitle();
        logger.info("approximate : {}",bookDAO.findByTitleContaining(title).size());
        return bookDAO.findByTitleContaining(title); // Implement method in BookDAO (full-text search)
    }

    @Override
    protected boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
        List<BookAuthor> searchAuthors = searchFormData.getBookAuthors(); // Assuming a list of author names
        List<BookAuthor> bookAuthors = book.getBookAuthors(); // Assuming a list of author names in Book
        //logger.info("checkIfAuthorsMatch bookAuthorS: {}",bookAuthors.size());
        //logger.info("checkIfAuthorsMatch searchAuthorS: {}",searchAuthors.size());
        // Handling null values
        if (searchAuthors == null || bookAuthors == null) {
            return false;
        }

        // Check if all search authors are present in book authors (case-insensitive)
        for (BookAuthor searchAuthor : searchAuthors) {
            boolean found = false;
            for (BookAuthor bookAuthor : bookAuthors) {
                // Comparing author names (case-insensitive)
                logger.info("checkIfAuthorsMatch bookAuthor: {}",bookAuthor.getAuthorName());
                logger.info("checkIfAuthorsMatch searchAuthor: {}",searchAuthor.getAuthorName());
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
