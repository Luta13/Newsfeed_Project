package org.sparta.newsfeed.comment.repository;

import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.entity.CommentLike;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Query("SELECT CASE WHEN COUNT(cl) > 0 THEN TRUE ELSE FALSE END FROM CommentLike cl WHERE cl.comment = :comment AND cl.user = :user")
    boolean existsByCommentAndUser(Comment comment, User user);

    @Query("SELECT cl FROM CommentLike cl WHERE cl.comment = :comment AND cl.user = :user")
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);

    int countByComment(Comment comment);
}