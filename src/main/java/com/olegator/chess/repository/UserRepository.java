package com.olegator.chess.repository;

import com.olegator.chess.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    @EntityGraph(attributePaths = {"userTimestamp", "userMedia"})
    Optional<User> findForPageById(Long id);
}
