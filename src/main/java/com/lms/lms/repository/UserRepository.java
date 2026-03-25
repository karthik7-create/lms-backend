package com.lms.lms.repository;

import com.lms.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.manager.id = :managerId AND u.isActive = true")
    List<User> findActiveSubordinates(@Param("managerId") Long managerId);

    @Query("SELECT u FROM User u WHERE u.manager.id = :managerId")
    List<User> findByManagerId(@Param("managerId") Long managerId);
}