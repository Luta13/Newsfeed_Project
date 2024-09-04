package org.sparta.newsfeed.board.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.dto.BoardCreateRequestDto;
import org.sparta.newsfeed.board.dto.BoardGetResponseDto;
import org.sparta.newsfeed.board.dto.BoardRequestDto;
import org.sparta.newsfeed.board.dto.BoardUpdateRequestDto;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.entity.BoardLike;
import org.sparta.newsfeed.board.repository.BoardLikeRepository;
import org.sparta.newsfeed.board.repository.BoardRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardLikeRepository boardLikeRepository;

    public void createBoard(AuthUser authUser, BoardCreateRequestDto boardCreateRequestDto) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Board board = new Board(boardCreateRequestDto.getTitle(), boardCreateRequestDto.getContent(), user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardGetResponseDto> getBoard(AuthUser authUser, BoardRequestDto boardRequestDto) {
        PageRequest pageRequest = PageRequest.of(boardRequestDto.getPage() - 1, 10, Sort.by(boardRequestDto.getSort().getSort()).descending());
        return boardRepository.findById(authUser.getUserId(), boardRequestDto.getStartDt(), boardRequestDto.getEndDt(), pageRequest);
    }

    public void updateBoard(AuthUser authUser, Long boardId, BoardUpdateRequestDto boardUpdateRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
        boardRepository.save(board);
    }

    public void deleteBoard(AuthUser authUser, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        boardRepository.delete(board);
    }

    public void likeBoard(AuthUser authUser, Long boardId) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        //본인 게시물은 좋아요 불가능
        if (board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("본인 게시물 좋아요 불가능");
        }

        if (Objects.isNull(boardLikeRepository.findByUserAndBoard(user, board))) {
            // 좋아요
            BoardLike boardLike = new BoardLike(user, board);

            boardLikeRepository.save(boardLike);
        } else {
            throw new IllegalArgumentException("더 이상 누를 수 없습니다.");
        }
    }

    public void unlikeBoard(AuthUser authUser, Long boardId) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board);
        if (Objects.nonNull(boardLike)) {
            boardLikeRepository.delete(boardLike);
        } else {
            throw new IllegalArgumentException("좋아요가 눌러지지 않은 게시물입니다.");
        }
    }

}
