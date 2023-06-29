package com.ragnar.customer_management.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDAO {
    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }
    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }
    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.existsCustomersByEmail(email);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);

    }
    @Override
    public boolean existsById(Integer id) {
        return customerRepository.existsCustomersById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
       customerRepository.save(customer);
    }

}
