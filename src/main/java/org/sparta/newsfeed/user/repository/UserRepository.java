package org.sparta.newsfeed.user.repository;

import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default User findByIdOrElseThrow(long userId) {
        return findById(userId).orElseThrow(() -> new NoSuchElementException("해당 유저는 존재하지 않습니다."));
    }
    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new NoSuchElementException("해당 유저는 존재하지 않습니다."));
    }
}
