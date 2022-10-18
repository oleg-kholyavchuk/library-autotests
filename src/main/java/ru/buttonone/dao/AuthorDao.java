package ru.buttonone.dao;

import ru.buttonone.domain.Author;


import java.util.List;

public interface AuthorDao {
    List<Author> getAuthorsByFio(String fio);
}
