package com.example.onlinebookstore.dao;

import java.util.List;

import com.example.onlinebookstore.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryDAO extends JpaRepository<BookCategory, Integer> {

    List<BookCategory> findByCategoryname(String categoryname);
}