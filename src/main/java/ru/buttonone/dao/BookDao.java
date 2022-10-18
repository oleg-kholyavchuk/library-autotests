package ru.buttonone.dao;

import ru.buttonone.domain.Book;

import java.util.List;

@SuppressWarnings("All")
public interface BookDao {
    List<Book> getBooksByTitle(String title);

    String getBookIdByBookTitle(String title);

    long getId(long id);
}
