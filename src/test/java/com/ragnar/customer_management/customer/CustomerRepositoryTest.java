package com.ragnar.customer_management.customer;

import com.ragnar.customer_management.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomersByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.FEMALE

        );

        underTest.save(customer);

        // When
        var actual = underTest.existsCustomersByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomersById() {

        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                Gender.FEMALE

        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsCustomersById(id);

        // Then
        assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        var actual = underTest.existsCustomersByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }
    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existsCustomersById(id);

        // Then
        assertThat(actual).isFalse();
    }

}