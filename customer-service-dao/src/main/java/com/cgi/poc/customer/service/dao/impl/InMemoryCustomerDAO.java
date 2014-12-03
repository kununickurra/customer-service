package com.cgi.poc.customer.service.dao.impl;

import com.cgi.poc.customer.service.dao.CustomerDAO;
import com.cgi.poc.customer.service.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory DAO implementation with a concurrent HashMap.
 */

@Repository
public class InMemoryCustomerDAO implements CustomerDAO {

    private Map<String, Customer> storage = new ConcurrentHashMap<String, Customer>();

    @Override
    public Customer findByNationalNumber(String id) {
        return storage.get(id);
    }

    @Override
    public void store(Customer entity) {
        storage.put(entity.getNationalNumber(), entity);
    }
}
