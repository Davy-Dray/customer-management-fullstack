package com.ragnar.customer_management.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    boolean existsCustomersByEmail(String email);
    boolean existsCustomersById(Integer id);
}
