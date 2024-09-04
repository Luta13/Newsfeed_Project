package org.sparta.newsfeed.board.repository;

import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUser(User user, Pageable pageable);
}