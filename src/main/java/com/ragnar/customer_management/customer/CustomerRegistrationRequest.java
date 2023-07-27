package com.ragnar.customer_management.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age,

        Gender gender
) {
}
