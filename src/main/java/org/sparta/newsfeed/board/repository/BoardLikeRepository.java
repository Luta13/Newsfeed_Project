package org.sparta.newsfeed.board.repository;

import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.entity.BoardLike;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    BoardLike findByUserAndBoard(User user, Board board);
}
