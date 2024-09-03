package org.sparta.newsfeed.comment.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.service.CommentService;
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

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBoard(@PathVariable Long boardId) {
        return new ResponseEntity<>(commentService.getCommentsByBoard(boardId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, User user, Board board) {
        return new ResponseEntity<>(commentService.createComment(commentDto, user, board), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestParam String content
    ) {
        return new ResponseEntity<>(commentService.updateComment(commentId, content), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
