package com.example.onlinebookstore.dao;

import java.util.List;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDAO extends JpaRepository<Book, Integer> {

    Book findByBookid(int bookid);
    List<Book> findByTitleContaining(String title);
    List<Book> findByTitleAndBookAuthors(String title, List<BookAuthor> authors);
}