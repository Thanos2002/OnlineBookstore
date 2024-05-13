package com.example.onlinebookstore.dao;

import java.util.List;


import com.example.onlinebookstore.model.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorDAO extends JpaRepository<BookAuthor, Integer> {

    List<BookAuthor> findByAuthorName(String authorName);

}