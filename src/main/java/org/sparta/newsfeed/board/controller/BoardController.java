package org.sparta.newsfeed.board.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.dto.BoardCreateRequestDto;
import org.sparta.newsfeed.board.dto.BoardGetResponseDto;
import org.sparta.newsfeed.board.dto.BoardUpdateRequestDto;
import org.sparta.newsfeed.board.service.BoardService;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시물 작성
    @PostMapping("/boards")
    public ResponseEntity<ResponseDto<String>> createBoard(@RequestBody BoardCreateRequestDto boardCreateRequestDto, @Auth AuthUser authUser) {
        boardService.createBoard(boardCreateRequestDto, authUser);
        return ResponseEntity.ok(new ResponseDto<>(200 ,"" , "게시물 작성이 완료되었습니다."));
    }
    //게시물 조회
    @GetMapping("/boards")
    public ResponseEntity<ResponseDto<Page<BoardGetResponseDto>>> getBoard(@Auth AuthUser authUser,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BoardGetResponseDto> boards = boardService.getBoard(authUser, pageable);
        return ResponseEntity.ok(new ResponseDto<>(200, boards, "조회에 성공했습니다."));
    }
    //게시물 수정
    @PatchMapping("/boards/{id}")
    public ResponseEntity<ResponseDto<String>> updateBoard(@PathVariable Long id, @RequestBody BoardUpdateRequestDto boardUpdateRequestDto, @Auth AuthUser authUser) {
        boardService.updateBoard(id, boardUpdateRequestDto, authUser);;
        return ResponseEntity.ok(new ResponseDto<>(200, "","게시물 수정이 완료되었습니다."));
    }
    //게시물 삭제
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ResponseDto<String>> deleteBoard(@PathVariable Long id, @Auth AuthUser authUser) {
        boardService.deleteBoard(id, authUser);
        return ResponseEntity.ok(new ResponseDto<>(204, "", "게시물 삭제가 완료되었습니다." ));
    }
    //게시물 좋아요
    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<ResponseDto<String>> likeBoard(@PathVariable Long boardId, @Auth AuthUser authUser) {
        boardService.likeBoard(boardId, authUser);
        return ResponseEntity.ok(new ResponseDto<>(200, "",  "좋아요가 등록되었습니다."));
    }
    //게시물 좋아요 취소
    @PostMapping("/boards/{boardId}/like-cancel")
    public ResponseEntity<ResponseDto<String>> unlikeBoard(@PathVariable Long boardId, @Auth AuthUser authUser) {
        boardService.unlikeBoard(boardId, authUser);
        return ResponseEntity.ok(new ResponseDto<>(200, "",  "좋아요가 삭제되었습니다."));
    }
}
