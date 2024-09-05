package org.sparta.newsfeed.comment.repository;

import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.exception.NoSuchElementException;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.board.boardId = :boardId")
    List<Comment> findByBoardId(Long boardId);

    default Comment findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(() -> new NoSuchElementException(ErrorCode.COMMENT_NOT_FOUND));
    }
}