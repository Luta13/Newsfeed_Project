package org.sparta.newsfeed.comment.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.service.CommentService;
import org.sparta.newsfeed.comment.service.CommentLikeService;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
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
    public ResponseEntity<ResponseDto<List<CommentDto>>> getCommentsByBoard(@PathVariable Long boardId) {
        List<CommentDto> comments = commentService.getCommentsByBoard(boardId);
        ResponseDto<List<CommentDto>> response = new ResponseDto<>(200, comments, "댓글 조회에 성공했습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<ResponseDto<String>> createComment(@RequestBody CommentDto commentDto, @PathVariable Long boardId, @Auth AuthUser authUser) {
        commentService.createComment(commentDto, authUser, boardId);
        return new ResponseEntity<>(new ResponseDto<>(200, null, "댓글이 작성되었습니다."), HttpStatus.OK);
    }


    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseDto<String>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto,
            @Auth AuthUser authUser
    ) {
        commentService.updateComment(commentId, commentDto.getCommentContent(), authUser);
        ResponseDto<String> response = new ResponseDto<>(200, null, "댓글이 수정되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @Auth AuthUser authUser
    ) {
        commentService.deleteComment(commentId, authUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<ResponseDto<String>> likeComment(@PathVariable Long commentId, @Auth AuthUser authUser) {
        commentLikeService.likeComment(commentId, authUser);
        ResponseDto<String> response = new ResponseDto<>(200, null, "좋아요가 등록되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/like-cancel")
    public ResponseEntity<ResponseDto<String>> unlikeComment(@PathVariable Long commentId, @Auth AuthUser authUser) {
        commentLikeService.unlikeComment(commentId, authUser);
        ResponseDto<String> response = new ResponseDto<>(200, null, "좋아요가 취소되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}