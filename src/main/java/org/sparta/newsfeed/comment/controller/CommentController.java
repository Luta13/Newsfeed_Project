package org.sparta.newsfeed.comment.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.service.CommentService;
import org.sparta.newsfeed.comment.service.CommentLikeService;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.sparta.newsfeed.user.entity.User;
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
    public ResponseEntity<ResponseDto<String>> createComment(@RequestBody CommentDto commentDto, @RequestParam Long boardId, @Auth User authUser) {
        commentService.createComment(commentDto, authUser, boardId);
        return new ResponseEntity<>(new ResponseDto<>(200, "댓글이 작성되었습니다.", null), HttpStatus.OK);
    }


    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestParam String content,
            @Auth User authUser
    ) {
        return new ResponseEntity<>(commentService.updateComment(commentId, content, authUser), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @Auth User authUser
    ) {
        commentService.deleteComment(commentId, authUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId, @Auth User authUser) {
        commentLikeService.likeComment(commentId, authUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/like-cancel")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long commentId, @Auth User authUser) {
        commentLikeService.unlikeComment(commentId, authUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}