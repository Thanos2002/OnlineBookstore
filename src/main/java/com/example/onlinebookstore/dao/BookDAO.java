package com.example.onlinebookstore.dao;

import java.util.List;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.BookAuthor;
import com.example.onlinebookstore.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDAO extends JpaRepository<Book, Integer> {

    Book findByBookid(int bookid);
    List<Book> findByTitleContaining(String title);
    List<Book> findByTitleAndBookAuthors(String title, List<BookAuthor> authors);
    List<Book> findByBookCategory(BookCategory bookCategory);
    @Query("SELECT b FROM Book b JOIN b.bookAuthors ba WHERE ba IN :bookAuthors")
    List<Book> findByBookAuthors(@Param("bookAuthors") List<BookAuthor> bookAuthors);

}