package com.ragnar.customer_management.customer;

public record CustomerUpdateRequest(String email,
                                    String name,
                                    Integer age) {
}
