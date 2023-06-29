package com.ragnar.customer_management.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {
    private static final List<Customer> customers;
    static int id = 1;

    static {
        customers = new ArrayList<>();


    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteById(Integer id) {
        customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);

    }

    @Override
    public boolean existsById(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer customer) {
       customers.add(customer);
    }


}
