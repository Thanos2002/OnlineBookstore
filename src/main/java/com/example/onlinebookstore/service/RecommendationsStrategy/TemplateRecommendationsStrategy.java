package com.example.onlinebookstore.service.RecommendationsStrategy;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.RecommendationsFormData;
import com.example.onlinebookstore.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class TemplateRecommendationsStrategy implements RecommendationsStrategy {

    @Override
    public List<BookFormData> recommend(RecommendationsFormData recommendationsFormData , BookDAO bookDAO){
        List<Book> recommendedBooks = makeListOfBooks(recommendationsFormData,bookDAO);
        List<BookFormData> finalBooks = new ArrayList<>();
        for(Book book:recommendedBooks){
                BookFormData bookFormData = new BookFormData();
                bookFormData.setTitle(book.getTitle());
                bookFormData.setDescription(book.getDescription());
                bookFormData.setBookAuthors(book.getBookAuthors());
                bookFormData.setBookCategory(book.getBookCategory());
                bookFormData.setUser_profile(book.getUser_profile());
                bookFormData.setRequestingUsers(book.getRequestingUsers());
                bookFormData.setBookid(book.getBookid());
                finalBooks.add(bookFormData);
        }
        return finalBooks;

    }

    protected abstract List<Book> makeListOfBooks(RecommendationsFormData recomData, BookDAO bookDAO);


}
