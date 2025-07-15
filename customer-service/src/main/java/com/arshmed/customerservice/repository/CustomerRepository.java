package com.arshmed.customerservice.repository;

import com.arshmed.customerservice.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByAuthId(String authId);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
