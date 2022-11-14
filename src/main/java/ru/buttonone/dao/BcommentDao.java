package ru.buttonone.dao;

import ru.buttonone.domain.Bcomment;

import java.util.List;

@SuppressWarnings("All")
public interface BcommentDao {
    List<Bcomment> getBcommentsByNickname(String nickname);
}
