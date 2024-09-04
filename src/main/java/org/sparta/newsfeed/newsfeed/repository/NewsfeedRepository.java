package org.sparta.newsfeed.newsfeed.repository;

import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedResponseDto;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsfeedRepository extends JpaRepository<Friend, Long> {
    @Query("""
        select b.title AS title , b.content AS content , u.name as userName , 
               ifnull(count(c.commentId),0) as commentCnt, ifnull(count(bl.boardLikeId),0) as likeCnt, 
               b.createdAt AS createdAt , b.modifiedAt AS modifiedAt 
        from Board b 
            inner join User u on u.userId = b.user.userId 
            left join Comment c on c.board.boardId = b.boardId 
            left join BoardLike bl on bl.board.boardId = b.boardId 
        where date_format(b.createdAt , '%Y-%m-%d') between :startDt and :endDt
          and b.user.userId in :userIds
        group by b.boardId
        """)
    Page<NewsfeedResponseDto> findByNewsfeed(List<Long> userIds, String startDt, String endDt, Pageable pageable);

    List<Friend> findByBaseIdAndApplyYnTrue(User baseId);
}
