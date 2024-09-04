package org.sparta.newsfeed.board.repository;

import org.sparta.newsfeed.board.dto.BoardGetResponseDto;
import org.sparta.newsfeed.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("""
        select b.title AS title , b.content AS content , u.name AS userName ,
               ifnull(count(c.commentId),0) as commentCnt, ifnull(count(bl.boardLikeId),0) as likeCnt, 
               b.createdAt AS createdAt , b.modifiedAt AS modifiedAt 
        from Board b
            inner join User u on (u.userId = b.user.userId)
            left join Comment c on (c.board.boardId = b.boardId)
            left join BoardLike bl on (bl.board.boardId = b.boardId)
        where b.user.userId = :userId
            and date_format(b.createdAt , '%Y-%m-%d') between :startDt and :endDt
        group by b.boardId
    """)
    Page<BoardGetResponseDto> findById(Long userId , String startDt, String endDt, Pageable pageable);
}
