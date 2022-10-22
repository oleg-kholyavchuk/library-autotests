package ru.buttonone.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.buttonone.domain.Author;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@SuppressWarnings("ALL")
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Author> getAuthorsByFio(String fio) {
        return jdbc.query("select id, fio from authors where fio = :fio", new AuthorsMapper());
    }


    private static class AuthorsMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(rs.getString("id"), rs.getString("fio"));
        }
    }
}
