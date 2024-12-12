package com.github.board.repository.auth.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u JOIN FETCH u.role r WHERE u.email = :email")
    Optional<Users> findByEmailFetchJoin(String email);

    boolean existsByEmail(String email);

}
