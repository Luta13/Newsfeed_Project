package org.sparta.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.entity.CommentLike;
import org.sparta.newsfeed.comment.repository.CommentLikeRepository;
import org.sparta.newsfeed.comment.repository.CommentRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
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
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        User user = userService.findById(authUser.getUserId());

        boolean alreadyLiked = commentLikeRepository.existsByCommentAndUser(comment, user);
        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 이 댓글에 좋아요를 눌렀습니다.");
        }

        CommentLike commentLike = new CommentLike(null, user, comment);
        commentLikeRepository.save(commentLike);
    }

    public void unlikeComment(Long commentId, AuthUser authUser) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        User user = userService.findById(authUser.getUserId());

        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 존재하지 않습니다."));

        commentLikeRepository.delete(commentLike);
    }

}