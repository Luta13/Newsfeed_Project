package org.sparta.newsfeed.board.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.entity.BoardLike;
import org.sparta.newsfeed.board.exception.BoardLikeException;
import org.sparta.newsfeed.board.repository.BoardLikeRepository;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardLikeService {

    private final UserService userService;
    private final BoardService boardService;
    private final BoardLikeRepository boardLikeRepository;

    public void likeBoard(AuthUser authUser, Long boardId) {
        User user = userService.findById(authUser.getUserId());
        Board board = boardService.findById(boardId);

        //본인 게시물은 좋아요 불가능
        if (board.getUser().getUserId().equals(authUser.getUserId())) {
            throw new BoardLikeException(ErrorCode.CANNOT_LIKE_OWN_BOARD);
        }

        if (Objects.isNull(boardLikeRepository.findByUserAndBoard(user, board))) {
            // 좋아요
            BoardLike boardLike = new BoardLike(user, board);

            boardLikeRepository.save(boardLike);
        } else {
            throw new BoardLikeException(ErrorCode.LIKE_LIMIT_EXCEEDED);
        }
    }

    public void unlikeBoard(AuthUser authUser, Long boardId) {
        User user = userService.findById(authUser.getUserId());
        Board board = boardService.findById(boardId);

        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board);
        if (Objects.nonNull(boardLike)) {
            boardLikeRepository.delete(boardLike);
        } else {
            throw new BoardLikeException(ErrorCode.LIKE_NOT_FOUND);
        }
    }
}
