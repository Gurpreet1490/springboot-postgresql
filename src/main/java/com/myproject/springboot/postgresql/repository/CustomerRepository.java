package com.myproject.springboot.postgresql.repository;

import com.myproject.springboot.postgresql.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.springboot.postgresql.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
