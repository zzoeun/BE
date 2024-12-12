package com.github.board.repository.good;

import com.github.board.repository.auth.user.Users;
import com.github.board.repository.post.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodRepository extends JpaRepository<Goods,Integer> {

    Optional<Goods> findByUserAndPost(Users user, Posts posts);
}
