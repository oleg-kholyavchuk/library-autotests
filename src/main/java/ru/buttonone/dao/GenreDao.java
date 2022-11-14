package ru.buttonone.dao;

import ru.buttonone.domain.Genre;

import java.util.List;

@SuppressWarnings("All")
public interface GenreDao {
    List<Genre> getGenresByName(String name);
}
