package com.github.board.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Posts, Long> {

    Optional<Posts> findByIdx(Integer idx);

    @Query("SELECT p FROM Posts p JOIN FETCH p.user u WHERE u.email = :email")
    List<Posts> findByUserEmail(@Param("email") String email);

    @Query("SELECT p FROM Posts p JOIN FETCH p.user u")
    List<Posts> findAllPost();
}