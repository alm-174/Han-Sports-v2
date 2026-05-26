package com.javaweb.repository;

import com.javaweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    boolean existsById(long id);
    Optional<User> findByEmail(String Email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
}
