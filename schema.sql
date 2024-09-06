-- MySQL 데이터베이스 구조 내보내기
DROP DATABASE IF EXISTS `newsfeed`;
CREATE DATABASE IF NOT EXISTS `newsfeed` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `newsfeed`;

-- 테이블 newsfeed.user 구조 내보내기 (가장 먼저 생성)
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
                                      `user_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                      `email` VARCHAR(255) NOT NULL,
                                      `password` VARCHAR(255) NOT NULL,
                                      `name` VARCHAR(255) NOT NULL,
                                      `status` ENUM('ACTIVE', 'REMOVE') NOT NULL,
                                      `created_at` DATETIME(6) DEFAULT NULL,
                                      `modified_at` DATETIME(6) DEFAULT NULL,
                                      PRIMARY KEY (`user_id`),
                                      UNIQUE KEY `UK_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 newsfeed.user 내보내기
INSERT INTO `user` (`user_id`, `email`, `password`, `name`, `status`, `created_at`, `modified_at`) VALUES
(1, 'test1234@test.com', '$2a$04$ByjZcaHkeFaXIOWiYwDzqOc9QCiBtGndrpwHYXEtwGF6OQV79YqHi', '123', 'ACTIVE', '2024-09-04 21:09:57.515826', '2024-09-04 21:09:57.515826'),
(2, 'test123@test.com', '$2a$04$pNUpU2kHklfMb8.yJdHnw.FxUX17lFCLX9wfQdSFFG9dZkV8d3TIi', '123', 'ACTIVE', '2024-09-04 21:10:00.915497', '2024-09-04 21:10:00.915497');

-- 테이블 newsfeed.board 구조 내보내기
DROP TABLE IF EXISTS `board`;
CREATE TABLE IF NOT EXISTS `board` (
                                       `board_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                       `title` VARCHAR(50) NOT NULL,
                                       `content` VARCHAR(200) NOT NULL,
                                       `user_id` BIGINT(20) NOT NULL,
                                       `created_at` DATETIME(6) DEFAULT NULL,
                                       `modified_at` DATETIME(6) DEFAULT NULL,
                                       PRIMARY KEY (`board_id`),
                                       KEY `FK_board_user` (`user_id`),
                                       CONSTRAINT `FK_board_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 newsfeed.board 내보내기
INSERT INTO `board` (`board_id`, `title`, `content`, `user_id`, `created_at`, `modified_at`) VALUES
(1, '게시물 제목124124', '게시물 내용34543', 2, '2024-09-04 21:10:37.971725', '2024-09-04 21:10:37.971725'),
(2, '게시물 제목124124', '게시물 내용34543', 2, '2024-09-04 21:10:41.329541', '2024-09-04 21:10:41.329541');

-- 테이블 newsfeed.board_like 구조 내보내기
DROP TABLE IF EXISTS `board_like`;
CREATE TABLE IF NOT EXISTS `board_like` (
                                            `board_like_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                            `board_id` BIGINT(20) NOT NULL,
                                            `user_id` BIGINT(20) NOT NULL,
                                            `created_at` DATETIME(6) DEFAULT NULL,
                                            `modified_at` DATETIME(6) DEFAULT NULL,
                                            PRIMARY KEY (`board_like_id`),
                                            KEY `FK_board_like_board` (`board_id`),
                                            KEY `FK_board_like_user` (`user_id`),
                                            CONSTRAINT `FK_board_like_board` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`),
                                            CONSTRAINT `FK_board_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 newsfeed.board_like 내보내기
INSERT INTO `board_like` (`board_like_id`, `board_id`, `user_id`, `created_at`, `modified_at`) VALUES
(1, 1, 1, '2024-09-04 21:11:44.063231', '2024-09-04 21:11:44.063231');

-- 테이블 newsfeed.comment 구조 내보내기
DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
                                         `comment_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                         `content` VARCHAR(255) NOT NULL,
                                         `board_id` BIGINT(20) NOT NULL,
                                         `user_id` BIGINT(20) NOT NULL,
                                         `created_at` DATETIME(6) DEFAULT NULL,
                                         `modified_at` DATETIME(6) DEFAULT NULL,
                                         PRIMARY KEY (`comment_id`),
                                         KEY `FK_comment_board` (`board_id`),
                                         KEY `FK_comment_user` (`user_id`),
                                         CONSTRAINT `FK_comment_board` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`),
                                         CONSTRAINT `FK_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 newsfeed.comment 내보내기
INSERT INTO `comment` (`comment_id`, `content`, `board_id`, `user_id`, `created_at`, `modified_at`) VALUES
(1, '댓글작성123', 1, 2, '2024-09-04 21:10:57.281286', '2024-09-04 21:10:57.281286');

-- 테이블 newsfeed.comment_like 구조 내보내기
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE IF NOT EXISTS `comment_like` (
                                              `comment_like_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                              `comment_id` BIGINT(20) NOT NULL,
                                              `user_id` BIGINT(20) NOT NULL,
                                              `created_at` DATETIME(6) DEFAULT NULL,
                                              `modified_at` DATETIME(6) DEFAULT NULL,
                                              PRIMARY KEY (`comment_like_id`),
                                              KEY `FK_comment_like_comment` (`comment_id`),
                                              KEY `FK_comment_like_user` (`user_id`),
                                              CONSTRAINT `FK_comment_like_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`),
                                              CONSTRAINT `FK_comment_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 newsfeed.friend 구조 내보내기
DROP TABLE IF EXISTS `friend`;
CREATE TABLE IF NOT EXISTS `friend` (
                                        `relation_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                        `base_id` BIGINT(20) NOT NULL,
                                        `friend_id` BIGINT(20) NOT NULL,
                                        `apply_yn` BIT(1) NOT NULL,
                                        `created_at` DATETIME(6) DEFAULT NULL,
                                        `modified_at` DATETIME(6) DEFAULT NULL,
                                        PRIMARY KEY (`relation_id`),
                                        KEY `FK_friend_base` (`base_id`),
                                        KEY `FK_friend_friend` (`friend_id`),
                                        CONSTRAINT `FK_friend_base` FOREIGN KEY (`base_id`) REFERENCES `user` (`user_id`),
                                        CONSTRAINT `FK_friend_friend` FOREIGN KEY (`friend_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
