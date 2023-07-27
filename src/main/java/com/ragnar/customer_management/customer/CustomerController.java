package com.ragnar.customer_management.customer;

import com.ragnar.customer_management.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers/")
public class CustomerController {

    private final JWTUtil jwtUtil;
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "{id}")
    public Customer getCustomer(@PathVariable("id") Integer id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
        String token = jwtUtil.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }

    @DeleteMapping(path = "{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        customerService.deleteCustomerById(id);
    }

    @PutMapping(path = "{id}")
    public void updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerUpdateRequest updateRequest
    ) {
        customerService.updateCustomer(id, updateRequest);
    }
}
