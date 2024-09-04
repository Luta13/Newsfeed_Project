package org.sparta.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.entity.CommentLike;
import org.sparta.newsfeed.comment.repository.CommentRepository;
import org.sparta.newsfeed.comment.repository.CommentLikeRepository;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.repository.BoardRepository;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;

    public CommentDto createComment(CommentDto commentDto, User user, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Comment comment = new Comment(null, commentDto.getContent(), user, board, null);
        Comment savedComment = commentRepository.save(comment);

        CommentLike commentLike = new CommentLike(null, user, savedComment);
        commentLikeRepository.save(commentLike);

        return convertToDto(savedComment);
    }

    public List<CommentDto> getCommentsByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommentDto updateComment(Long commentId, String content, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Board board = boardRepository.findById(comment.getBoard().getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자 본인만 댓글을 수정할 수 있습니다.");
        }

        comment.setContent(content);
        return convertToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Board board = boardRepository.findById(comment.getBoard().getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자 본인만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getUser().getUserId(),
                comment.getBoard().getBoardId()
        );
    }

}
