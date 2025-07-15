package com.arshmed.authservice.repository;

import com.arshmed.authservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
    Optional<UserCredential> findByEmail(String email);
    boolean existsByEmail(String email);
}