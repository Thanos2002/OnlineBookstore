package com.example.onlinebookstore.service.RecommendationsStrategy;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.RecommendationsFormData;
import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriesRecommendationsStrategy extends TemplateRecommendationsStrategy {

    @Override
    protected List<Book> makeListOfBooks(RecommendationsFormData recomData, BookDAO bookDAO) {
        List<BookCategory> favouriteCategories = recomData.getUserProfileFormData().getFavouriteCategories();
        List<Book> recommendedBooks = new ArrayList<>();
        for(BookCategory bookCategory: favouriteCategories){
            List<Book> categoryBooks = bookDAO.findByBookCategory(bookCategory);
            recommendedBooks.addAll(categoryBooks);
        }
        return recommendedBooks;
    }
}
