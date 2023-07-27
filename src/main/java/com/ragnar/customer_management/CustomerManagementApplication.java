package com.ragnar.customer_management;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.ragnar.customer_management.customer.Customer;
import com.ragnar.customer_management.customer.CustomerRepository;
import com.ragnar.customer_management.customer.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class CustomerManagementApplication implements CommandLineRunner {

    @Autowired
    CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        var faker = new Faker();
        Random random = new Random();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        int age = random.nextInt(16, 99);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@ragnar.com";
        Customer customer = new Customer(
                firstName +  " " + lastName,
                email,
                "password", age,
                gender
        );
        customerRepository.save(customer);
    }
}
