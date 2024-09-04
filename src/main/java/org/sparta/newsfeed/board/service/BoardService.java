package org.sparta.newsfeed.board.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.dto.BoardCreateRequestDto;
import org.sparta.newsfeed.board.dto.BoardGetResponseDto;
import org.sparta.newsfeed.board.dto.BoardRequestDto;
import org.sparta.newsfeed.board.dto.BoardUpdateRequestDto;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.repository.BoardRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;

    public void createBoard(AuthUser authUser, BoardCreateRequestDto boardCreateRequestDto) {
        User user = userService.findById(authUser.getUserId());

        Board board = new Board(boardCreateRequestDto.getTitle(), boardCreateRequestDto.getContent(), user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<BoardGetResponseDto> getBoard(AuthUser authUser, BoardRequestDto boardRequestDto) {
        PageRequest pageRequest = PageRequest.of(boardRequestDto.getPage() - 1, 10, Sort.by(boardRequestDto.getSort().getSort()).descending());
        return boardRepository.findById(authUser.getUserId(), boardRequestDto.getStartDt(), boardRequestDto.getEndDt(), pageRequest);
    }

    public void updateBoard(AuthUser authUser, Long boardId, BoardUpdateRequestDto boardUpdateRequestDto) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
        boardRepository.save(board);
    }

    public void deleteBoard(AuthUser authUser, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        boardRepository.delete(board);
    }

    public Board findById(Long boardId) {
        return boardRepository.findByIdOrElseThrow(boardId);
    }

}
