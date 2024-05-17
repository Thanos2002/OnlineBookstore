package com.example.onlinebookstore.service.RecommendationsStrategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.RecommendationsFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.model.BookCategory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorsRecommendationsStrategy extends TemplateRecommendationsStrategy {
    @Override
    protected List<Book> makeListOfBooks(RecommendationsFormData recomData, BookDAO bookDAO) {
        List<BookAuthor> favouriteAuthors = recomData.getUserProfileFormData().getFavouriteAuthors();
        return bookDAO.findByBookAuthors(favouriteAuthors);
    }
}
