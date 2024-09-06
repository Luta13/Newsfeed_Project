package org.sparta.newsfeed.user.repository;

import jakarta.persistence.LockModeType;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.NotFoundException;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 비관적 잠금을 통해 이메일 중복 확인 시 다른 트랜잭션에서 해당 이메일을 사용하지 못하도록 잠금을 걸 수 있습니다
    // 동시에 여러 사용자가 동일한 이메일을 등록하려고 할 때 발생할 수 있는 충돌을 방지 +
    // 다른 트랜잭션에서 해당 이메일을 사용하지 못하도록 잠금을 걸 수 있습니다
    // 백엔드에서는 포스트맨을 통해 테스트할떄 작동이 잘 되어도 프론트엔드에서는 작동이 안될 수 있어서 해결하기 위함입니다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByEmail(String email);

    default User findByIdOrElseThrow(long userId) {
        return findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
