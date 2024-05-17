package com.example.onlinebookstore.service.RecommendationsStrategy;

import com.example.onlinebookstore.dao.BookDAO;
import com.example.onlinebookstore.formsdata.BookFormData;
import com.example.onlinebookstore.formsdata.RecommendationsFormData;

import java.util.List;

public interface RecommendationsStrategy {
    List<BookFormData> recommend(RecommendationsFormData recommendationsFormData , BookDAO bookDAO);
}