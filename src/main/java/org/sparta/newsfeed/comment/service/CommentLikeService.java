package org.sparta.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.entity.CommentLike;
import org.sparta.newsfeed.comment.exception.custom.AlreadyLikedCommentException;
import org.sparta.newsfeed.comment.exception.custom.LikeDoesNotExistException;
import org.sparta.newsfeed.comment.exception.custom.NoSuchElementException;
import org.sparta.newsfeed.comment.repository.CommentLikeRepository;
import org.sparta.newsfeed.comment.repository.CommentRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public void likeComment(Long commentId, AuthUser authUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.COMMENT_NOT_FOUND));

        User user = userService.findById(authUser.getUserId());

        if (user == null) {
            throw new NoSuchElementException(ErrorCode.USER_NOT_FOUND);
        }

        boolean alreadyLiked = commentLikeRepository.existsByCommentAndUser(comment, user);
        if (alreadyLiked) {
            throw new AlreadyLikedCommentException(ErrorCode.ALREADY_LIKED_COMMENT);
        }

        CommentLike commentLike = new CommentLike(null, user, comment);
        commentLikeRepository.save(commentLike);
    }

    public void unlikeComment(Long commentId, AuthUser authUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.COMMENT_NOT_FOUND));

        User user = userService.findById(authUser.getUserId());

        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user)
                .orElseThrow(() -> new LikeDoesNotExistException(ErrorCode.LIKE_DOES_NOT_EXIST));

        commentLikeRepository.delete(commentLike);
    }

}