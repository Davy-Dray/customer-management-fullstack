package com.ragnar.customer_management.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate,
                                         CustomerRowMapper customerRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id,name,email, age
                FROM customer
                """;
        return jdbcTemplate.query(sql,customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {

        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;
        return  jdbcTemplate.query(sql,customerRowMapper,id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
               INSERT INTO customer(name, email, age)
               VALUES(?,?,?)               \s
               """;
       jdbcTemplate.update(
               sql,
               customer.getName(),
               customer.getEmail(),
               customer.getAge()
       );
    }

    @Override
    public boolean existsByEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Integer id) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("deleteCustomerById result = " + result);
    }

    @Override
    public boolean existsById(Integer id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
        }
        if (customer.getAge() != null) {
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
        }
        if (customer.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId());
        }
    }
}
