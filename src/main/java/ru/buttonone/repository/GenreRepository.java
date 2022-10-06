package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    @Query("select g.id, name from genres g join books b on b.GENRE_ID = g.id where g.name =:name")
    List<Genre> getGenresByName(@Param("name") String name);
}
