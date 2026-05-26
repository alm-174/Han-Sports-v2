package com.javaweb.repository;

import com.javaweb.domain.Order;
import com.javaweb.domain.User;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByUserAndId(User user, Long id);
<<<<<<< HEAD
    Page<Order> findByUser(User user, Pageable pageable);
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
}
