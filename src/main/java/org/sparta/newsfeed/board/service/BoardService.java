package org.sparta.newsfeed.board.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.dto.*;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.repository.BoardRepository;
import org.sparta.newsfeed.comment.dto.CommentDto;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.repository.CommentRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.ForbiddenException;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new ForbiddenException(ErrorCode.AUTHOR_ONLY_CAN_EDIT);
        }

        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
        boardRepository.save(board);
    }

    public void deleteBoard(AuthUser authUser, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        if (!board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new ForbiddenException(ErrorCode.AUTHOR_ONLY_CAN_DELETE);
        }

        boardRepository.delete(board);
    }

    public Board findById(Long boardId) {
        return boardRepository.findByIdOrElseThrow(boardId);
    }

    // html 파일에서 게시물 상세 조회 시 사용 (게시물 작성자, 게시물 좋아요 , 댓글 목록은 필요 없음)
    @Transactional(readOnly = true)
    public BoardDetailsResponseDto getBoardDetails(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        return new BoardDetailsResponseDto(board.getBoardId(), board.getTitle(), board.getContent(), board.getUser().getName(), board.getBoardLikeList().size());
    }


}
