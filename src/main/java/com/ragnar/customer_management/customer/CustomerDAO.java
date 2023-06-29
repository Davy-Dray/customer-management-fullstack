package com.ragnar.customer_management.customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerDAO {
    List<Customer> selectAllCustomers();
    Optional<Customer> getCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsByEmail(String email);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    void updateCustomer(Customer customer);
}