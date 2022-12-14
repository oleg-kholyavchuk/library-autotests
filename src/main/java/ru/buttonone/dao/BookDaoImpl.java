package ru.buttonone.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.buttonone.domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@SuppressWarnings("ALL")
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Book> getBooksByTitle(String title) {

        return jdbc.query("select b.id, b.title, a.fio, g.name from books b join books_authors ba on b.id = ba.book_id join authors a on ba.author_id = a.id join genres g on b.genre_id  = g.id where b.title = :title",
                new BooksMapper());
    }

    @Override
    public String getBookIdByBookTitle(String title) {
        return jdbc.getJdbcOperations().queryForObject("select b.id from books b where b.title = ?", new Object[]{title}, String.class);
    }

    private static class BooksMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(rs.getString("id"), rs.getString("title"), rs.getString("authors"), rs.getString("genre"));
        }
    }
}
