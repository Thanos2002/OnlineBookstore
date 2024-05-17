package com.example.onlinebookstore.service.Strategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ExactSearchStrategy extends TemplateSearchStrategy {

    @Override
    protected List<Book> makeInitialListOfBooks(SearchFormData searchFormData,BookDAO bookDAO) {
        String title = searchFormData.getTitle();
        List<BookAuthor> authors = searchFormData.getBookAuthors(); // Assuming a list of author names
        return bookDAO.findByTitleAndBookAuthors(title, authors);//bookDAO.findByExactTitleAndAuthors(title, authors); // Implement method in BookDAO
    }

    @Override
    protected boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
        // This method is not needed for exact search since authors are already matched in makeInitialListOfBooks
        return true;
    }
}
