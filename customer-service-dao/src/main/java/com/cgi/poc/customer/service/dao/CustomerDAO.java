package com.cgi.poc.customer.service.dao;

/**
 * Customer DAO Interface
 */
public interface CustomerDAO<K,V> {

    public V findByNationalNumber(K nationalNumber);

    public void store(V entity);
}
