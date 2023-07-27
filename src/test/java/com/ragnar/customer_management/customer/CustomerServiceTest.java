package com.ragnar.customer_management.customer;

import com.ragnar.customer_management.exception.DuplicateResourceException;
import com.ragnar.customer_management.exception.RequestValidationException;
import com.ragnar.customer_management.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void getAllCustomers() {
        //WHEN
        underTest.getAllCustomers();

        //THEN
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id,
                "dave",
                "dave@gmail.com",
                "password", 20
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        Customer actual = underTest.getCustomer(10);
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //GIVEN
        int id = 10;

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.empty());

        //WHEN
        //THEN
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "customer [%s] not found".formatted(id)
                );
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email = "alex@gmail.com";

        when(customerDAO.existsByEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19 ,Gender.FEMALE);

        // When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDAO, never()).insertCustomer(any());
    }

    @Test
    void addCustomer() {
        String email = "dave@gmail.com";

        when(customerDAO.existsByEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "dave",
                email,
                20,
                Gender.FEMALE
        );
        underTest.addCustomer(request);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDAO).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getId()).isNull();

    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 10;

        when(customerDAO.existsById(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);
        // Then
        verify(customerDAO).deleteById(id);
    }

    @Test
    void willThrowDeleteCustomerByIdNotExists() {
        // Given
        int id = 10;

        when(customerDAO.existsById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest
                .deleteCustomerById(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer [%s] not found".formatted(id)
                );

        // Then
        verify(customerDAO, never()).deleteById(id);
    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "dave",
                "dave@gmail.com",
                "password", 20
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest (newEmail,"davido", 23 ,Gender.MALE);

        when(customerDAO.existsByEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }


    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19);
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest( null,"Alexandro", null,null);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19);
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest( newEmail,null, null,null);

        when(customerDAO.existsByEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19);
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 22,null);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19);
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest (newEmail,null, null,null);

        when(customerDAO.existsByEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest)).isInstanceOf(DuplicateResourceException.class).hasMessage("email already taken");

        // Then
        verify(customerDAO, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 10;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", "password", 19);
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getEmail(), customer.getName(), customer.getAge(), customer.getGender());

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest)).isInstanceOf(RequestValidationException.class).hasMessage("no data changes found");

        // Then
        verify(customerDAO, never()).updateCustomer(any());
    }


}