package com.myproject.springboot.postgresql.repository;

import com.myproject.springboot.postgresql.model.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
    public interface CustomerRepository extends JpaRepository<Customer, Integer> {

        Optional<Customer> findByEmailIdAndPassword(String emailId, String password);

        Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

        Optional<Customer> findById(Long customerId);
       // Optional<Customer> findByEmailId(String emailId);

        Customer findByPasswordToken(String token);

        Customer findByEmailId(String emailId);

    }
