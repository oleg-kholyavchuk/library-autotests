package ru.buttonone.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.buttonone.domain.Bcomment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@SuppressWarnings("ALL")
@RequiredArgsConstructor
public class BcommentDaoImpl implements BcommentDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Bcomment> getBcommentsByNickname(String nickname) {

        return jdbc.query("select b.id, book_id, nickname, message from B_COMMENTS b join books bo on bo.id = b.book_id where b.nickname = nickname",
                new BcommentsMapper());
    }

    private static class BcommentsMapper implements RowMapper<Bcomment> {
        @Override
        public Bcomment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Bcomment(rs.getString("id"), rs.getString("bookId"),
                    rs.getString("nickname"), rs.getString("message"));
        }
    }
}
