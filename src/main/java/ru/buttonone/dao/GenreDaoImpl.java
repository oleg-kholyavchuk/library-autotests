package ru.buttonone.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.buttonone.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@SuppressWarnings("ALL")
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> getGenresByName(String name) {
        return jdbc.query("select g.id, name from genres g join books b on b.GENRE_ID = g.id where g.name = name", new GenresMapper());
    }

    private static class GenresMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getString("id"), rs.getString("name"));
        }
    }
}
