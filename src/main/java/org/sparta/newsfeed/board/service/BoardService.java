package org.sparta.newsfeed.board.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.dto.*;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.entity.BoardLike;
import org.sparta.newsfeed.board.repository.BoardLikeRepository;
import org.sparta.newsfeed.board.repository.BoardRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional
    public BoardCreateResponseDto createBoard(BoardCreateRequestDto boardCreateRequestDto, AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Board board = new Board(
                boardCreateRequestDto.getTitle(),
                boardCreateRequestDto.getContent(),
                user
        );

        Board savedBoard = boardRepository.save(board);

        return new BoardCreateResponseDto(
                savedBoard.getBoardId(),
                savedBoard.getTitle(),
                savedBoard.getContent(),
                savedBoard.getUser().getName(),
                savedBoard.getCreatedAt()
        );

    }

    public Page<BoardGetResponseDto> getBoard(AuthUser authUser, Pageable pageable) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return boardRepository.findAllByUser(user, pageable)
                .map(board -> new BoardGetResponseDto(
                        board.getTitle(),
                        board.getContent(),
                        board.getUser().getName(),
                        board.getCommentList().size(),
                        board.getBoardLikeList().size(),
                        board.getCreatedAt(),
                        board.getModifiedAt()
                ));
    }


    @Transactional
    public BoardUpdateResponseDto updateBoard(Long id, BoardUpdateRequestDto boardUpdateRequestDto, AuthUser authUser) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("게시물을 작성한 작성자가 아닙니다.");
        }

        board.update(
                boardUpdateRequestDto.getTitle(),
                boardUpdateRequestDto.getContent()
        );

        return new BoardUpdateResponseDto(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getName(),
                board.getModifiedAt()
        );

    }

    @Transactional
    public void deleteBoard(Long id, AuthUser authUser) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("게시물을 작성한 작성자가 아닙니다.");


        }
        boardRepository.delete(board);
    }

    @Transactional
    public void likeBoard(Long boardId, AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        //본인 게시물은 좋아요 불가능
        if (board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("작성한 게시물에는 좋아요를 누를 수 없습니다.");
        }

        //동일 사용자가 같은 게시물에 좋아요 요청시 예외처리
        if (boardLikeRepository.findByUserAndBoard(user, board).isPresent()) {
            throw new IllegalArgumentException("더 이상 누를 수 없습니다.");
        }

        // Repository 생성 힌트, 좋아요 저장
        BoardLike boardLike = new BoardLike(user, board);
        boardLikeRepository.save(boardLike);
    }

    @Transactional
    public void unlikeBoard(Long boardId, AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        //좋아요 눌렀는지 확인 필요
        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않았습니다."));

        //좋아요 삭제
        boardLikeRepository.delete(boardLike);
    }

}