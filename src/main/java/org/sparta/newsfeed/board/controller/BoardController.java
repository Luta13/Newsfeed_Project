package org.sparta.newsfeed.board.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.dto.BoardCreateRequestDto;
import org.sparta.newsfeed.board.dto.BoardGetResponseDto;
import org.sparta.newsfeed.board.dto.BoardRequestDto;
import org.sparta.newsfeed.board.dto.BoardUpdateRequestDto;
import org.sparta.newsfeed.board.service.BoardLikeService;
import org.sparta.newsfeed.board.service.BoardService;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardLikeService boardLikeService;

    //게시물 작성
    @PostMapping
    public ResponseEntity<ResponseDto<String>> createBoard(@Auth AuthUser authUser , @RequestBody BoardCreateRequestDto boardCreateRequestDto) {
        boardService.createBoard(authUser , boardCreateRequestDto);
        return ResponseEntity.ok(new ResponseDto<>(200 ,"" , "게시물 작성이 완료되었습니다."));
    }
    //게시물 조회
    @GetMapping
    public ResponseEntity<ResponseDto<Page<BoardGetResponseDto>>> getBoard(@Auth AuthUser authUser, @ModelAttribute BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(new ResponseDto<>(200, boardService.getBoard(authUser, boardRequestDto), "조회에 성공했습니다."));
    }
    //게시물 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<ResponseDto<String>> updateBoard(@Auth AuthUser authUser , @PathVariable Long boardId, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto) {
        boardService.updateBoard(authUser , boardId, boardUpdateRequestDto);;
        return ResponseEntity.ok(new ResponseDto<>(200, "","게시물 수정이 완료되었습니다."));
    }
    //게시물 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<String>> deleteBoard(@Auth AuthUser authUser , @PathVariable Long boardId) {
        boardService.deleteBoard(authUser , boardId);
        return ResponseEntity.ok(new ResponseDto<>(204, "", "게시물 삭제가 완료되었습니다." ));
    }
    //게시물 좋아요
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ResponseDto<String>> likeBoard(@Auth AuthUser authUser , @PathVariable Long boardId) {
        boardLikeService.likeBoard(authUser , boardId);
        return ResponseEntity.ok(new ResponseDto<>(200, "",  "좋아요가 등록되었습니다."));
    }
    //게시물 좋아요 취소
    @PostMapping("/{boardId}/like-cancel")
    public ResponseEntity<ResponseDto<String>> unlikeBoard(@Auth AuthUser authUser , @PathVariable Long boardId) {
        boardLikeService.unlikeBoard(authUser , boardId);
        return ResponseEntity.ok(new ResponseDto<>(200, "",  "좋아요가 삭제되었습니다."));
    }
}
