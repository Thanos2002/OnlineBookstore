package com.example.onlinebookstore.service.Strategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.SearchFormData;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SearchStrategy {
    List<BookFormData> search(SearchFormData bookFormData , BookDAO bookDAO);
}
