package com.ragnar.customer_management.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.ragnar.customer_management.customer.Customer;
import com.ragnar.customer_management.customer.CustomerRegistrationRequest;
import com.ragnar.customer_management.customer.CustomerUpdateRequest;
import com.ragnar.customer_management.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {

    private static final Random RANDOM = new Random();
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterACustomer() {

        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@gmail.com";
        String password = "password";
        int age = RANDOM.nextInt(1, 30);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                Gender.FEMALE
        );

        String uri = "/api/v1/customers/";
        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customers
        List<Customer> customerList = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult()
                .getResponseBody();

        Customer expected = new Customer(
                name,
                email,
                password,
                age,
                Gender.FEMALE

        );
        assertThat(customerList)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);

        assert customerList != null;
        var id = customerList.stream()
                        .filter(customer -> customer.getEmail().equals(email))
                                .map(Customer::getId)
                                        .findFirst()
                                                .orElseThrow();
        expected.setId(id);
        //get customer by id
        webTestClient.get()
                .uri(uri + "{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expected);


    }
    @Test
    void canDeleteCustomer(){
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@gmail.com";
        String password = "password";

        int age = RANDOM.nextInt(1, 30);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                Gender.FEMALE
        );

        String uri = "/api/v1/customers/";
        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customers
        List<Customer> customerList = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult()
                .getResponseBody();



        assert customerList != null;
        var id = customerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                        .uri(uri+"{id}",id)
                                .accept(MediaType.APPLICATION_JSON)
                                        .exchange()
                                                .expectStatus()
                                                        .isOk();


        //get customer by id
        webTestClient.get()
                .uri(uri + "{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();


    }
    @Test
    void canUpdateCustomer(){

        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1, 30);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age,
                Gender.FEMALE
        );

        String uri = "/api/v1/customers/";
        webTestClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customers
        List<Customer> customerList = webTestClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                }).returnResult()
                .getResponseBody();



        assert customerList != null;
        var id = customerList.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //update customer
        String newName = "newName";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(

                null,
                newName,
                null,
                null
        );
        

        webTestClient.put()
                .uri(uri+"{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        //get customer by id
        Customer updated = webTestClient.get()
                .uri(uri + "{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected= new Customer(
                id,
                newName,
                email,
                "password", age
        );
        assertThat(updated).isEqualTo(expected);


    }


}
