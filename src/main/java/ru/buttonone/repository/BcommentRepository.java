package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Bcomment;

import java.util.List;

public interface BcommentRepository extends CrudRepository<Bcomment, Long> {

    @Query("select b.id, book_id, nickname, message from B_COMMENTS b join books bo on bo.id = b.book_id where b.nickname =:nickname")
    List<Bcomment> getBcommentsByNickname(@Param("nickname") String nickname);
}
