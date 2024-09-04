package org.sparta.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.service.BoardService;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.repository.CommentLikeRepository;
import org.sparta.newsfeed.comment.repository.CommentRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;
    private final CommentLikeRepository commentLikeRepository;

    public void createComment(CommentDto commentDto, AuthUser authUser, Long boardId) {
        Board board = boardService.findById(boardId);
        User user = userService.findById(authUser.getUserId());
        Comment comment = new Comment(null, commentDto.getCommentContent(), user, board, null);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByBoard(Long boardId) {
        boardService.findById(boardId);

        List<Comment> comments = commentRepository.findByBoardId(boardId);

        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommentDto updateComment(Long commentId, String commentContent, AuthUser authUser) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if (!comment.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("작성자 본인만 댓글을 수정할 수 있습니다.");
        }

        comment.setContent(commentContent);
        return convertToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId, AuthUser user) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자 본인만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    private CommentDto convertToDto(Comment comment) {

        int commentLikeCount = commentLikeRepository.countByComment(comment);

        return new CommentDto(
                comment.getContent(),
                commentLikeCount,
                comment.getUser().getName(),
                comment.getBoard().getTitle(),
                comment.getBoard().getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}