package com.ragnar.customer_management.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
       //WHEN
        underTest.selectAllCustomers();

        //THEN
       verify(customerRepository)
               .findAll();
    }

    @Test
    void getCustomerById() {
        //GIVEN
        int id = 1;

        //WHEN
     underTest.getCustomerById(id);

        //THEN
        verify(customerRepository)
                .findById(id);
    }

    @Test
    void insertCustomer() {

        //GIVEN
        Customer customer = new Customer(
                1,
                "david",
                "d@gmail.com",
                20
        );
        //WHEN
        underTest.insertCustomer(customer);

        //THEN
        verify(customerRepository).save(customer);

    }

    @Test
    void existsByEmail() {
        //GIVEN
        String email = "dave@gmail.com";

        //WHEN
        underTest.existsByEmail(email);

        //THEN
        verify(customerRepository)
                .existsCustomersByEmail(email);


}
    @Test
    void existsCustomerById() {
        // Given
        int id = 1;

        // When
        underTest.existsById(id);

        // Then
        verify(customerRepository).existsCustomersById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 1;

        // When
        underTest.deleteById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                1,
                "david",
                "d@gmail.com",
                20
        );

        // When
        underTest.updateCustomer(customer);

        //THEN
        verify(customerRepository).save(customer);
    }
}