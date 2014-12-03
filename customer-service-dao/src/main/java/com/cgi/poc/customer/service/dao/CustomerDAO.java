package com.cgi.poc.customer.service.dao;

import com.cgi.poc.customer.service.entity.Customer;

/**
 * {@link Customer} DAO Interface.
 * Basic find and store method
 */
public interface CustomerDAO {
    /**
     * Search a {@link Customer} using its national number.
     * National Number is a unique identifier.
     *
     * @param nationalNumber the national number to search for.
     * @return The {@link Customer} that has the given national number or <code>null</code> if no {@link Customer} could
     * be found for the given national number.
     */
    public Customer findByNationalNumber(String nationalNumber);

    /**
     * Store a customer in the system.
     * If the customer already exists it will be replaced by the new customer entity.
     *
     * @param entity entity to store
     */
    public void store(Customer entity);
}
