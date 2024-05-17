package com.example.onlinebookstore.service.SearchStrategy;

import com.example.onlinebookstore.controller.AuthController;
import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.SearchFormData;
import com.example.onlinebookstore.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class TemplateSearchStrategy implements SearchStrategy {


    @Override
    public List<BookFormData> search(SearchFormData searchFormData, BookDAO bookDAO) {
        List<BookFormData> finalBooks = new ArrayList<>();
        List<Book> books = makeInitialListOfBooks(searchFormData,bookDAO); // Retrieve initial list of books
        for(Book book:books){
            if (checkIfAuthorsMatch(searchFormData, book)){
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
        }
        return finalBooks;
    }
    protected abstract List<Book> makeInitialListOfBooks(SearchFormData searchDto, BookDAO bookDAO);
    protected abstract boolean checkIfAuthorsMatch(SearchFormData searchFormData,Book book);
}
