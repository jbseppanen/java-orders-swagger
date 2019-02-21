package com.lambdaschool.javaorders.repository;

import com.lambdaschool.javaorders.models.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomersRepository extends JpaRepository<Customers, Long> {
    List<Customers> findByAgentcode_Agentcode(long agentcode);
}
