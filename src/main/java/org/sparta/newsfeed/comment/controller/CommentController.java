package org.sparta.newsfeed.comment.controller;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.service.CommentService;
import org.sparta.newsfeed.comment.service.CommentLikeService;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.board.entity.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBoard(@PathVariable Long boardId) {
        return new ResponseEntity<>(commentService.getCommentsByBoard(boardId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, User user, Board board) {
        return new ResponseEntity<>(commentService.createComment(commentDto, user, board.getBoardId()), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestParam String content,
            @NotNull User user
    ) {
        return new ResponseEntity<>(commentService.updateComment(commentId, content, user), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @NonNull User user
    ) {
        commentService.deleteComment(commentId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId, @RequestBody User user) {
        commentLikeService.likeComment(commentId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/like-cancel")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long commentId, @RequestBody User user) {
        commentLikeService.unlikeComment(commentId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
