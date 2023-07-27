package com.ragnar.customer_management.customer;

import com.ragnar.customer_management.exception.DuplicateResourceException;
import com.ragnar.customer_management.exception.RequestValidationException;
import com.ragnar.customer_management.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private  final  CustomerDAO  customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDAO.getCustomerById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        "customer [%s] not found".formatted(id)
                ));
    }
    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        String email = customerRegistrationRequest.email();
        if(customerDAO.existsByEmail(email)){
             throw  new DuplicateResourceException(
                     "email already taken"
             );
         }
        customerDAO.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        "password", customerRegistrationRequest.age(),
                        customerRegistrationRequest.gender()
                )
        );
    }
    public void deleteCustomerById(Integer id){
        if(!customerDAO.existsById(id)){
            throw new ResourceNotFoundException(
                    "customer [%s] not found".formatted(id)
            );
        }
        customerDAO.deleteById(id);
    }

    public void updateCustomer(Integer customerId,  CustomerUpdateRequest updateRequest){
        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDAO.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if (updateRequest.gender() != null && !updateRequest.gender().equals(customer.getGender())) {
            if (customerDAO.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setGender(updateRequest.gender());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }


        customerDAO.updateCustomer(customer);
    }
}
