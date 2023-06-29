package com.ragnar.customer_management.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers/")
public class CustomerController {

    private  final  CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping(path = "{id}")
    public Customer getCustomer(@PathVariable("id") Integer id){
        return customerService.getCustomer(id);
    }
    @PostMapping
    public void addCustomer(
            @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.addCustomer(customerRegistrationRequest);
    }

    @DeleteMapping(path ="{id}")
    public void deleteById(@PathVariable("id") Integer id){
        customerService.deleteCustomerById(id);
    }

    @PutMapping(path ="{id}")
    public void deleteCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerUpdateRequest updateRequest
    ){
        customerService.updateCustomer(id,updateRequest);
    }
}
