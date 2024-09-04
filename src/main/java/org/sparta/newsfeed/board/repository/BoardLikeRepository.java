package org.sparta.newsfeed.board.repository;

import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.entity.BoardLike;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    @Query("SELECT COUNT(bl) FROM BoardLike bl WHERE bl.board = :board")
    boolean existsByBoardAndUser(Board board, User user);

    @Query("SELECT bl FROM BoardLike bl WHERE bl.board = :board AND bl.user = :user")
    Optional<BoardLike> findByBoardAndUser(Board board, User user);

//    Optional<BoardLike> findByUserAndBoard(User user, Board board);
}
